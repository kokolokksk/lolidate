package monster.loli.lolidate.service.impl

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletResponse
import monster.loli.lolidate.service.UpdateService
import monster.loli.lolidate.utils.FileUtils
import monster.loli.lolidate.utils.FileZip
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.net.Proxy


@Service
class UpdateServiceImpl:UpdateService {
    val log: Logger = LoggerFactory.getLogger(this.javaClass)
    override fun getVersion(client: String): String {
        val builder = OkHttpClient().newBuilder()
        val url = "https://raw.githubusercontent.com/kokolokksk/catcat-dm/dom/version.json"
        builder.proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8889)))
        val okClient = builder.build()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        try {
            okClient.newCall(request).execute().use { response ->
                val data =  response.body!!.string()
                val gson=Gson()
                val dataMap = gson.fromJson(data,LinkedHashMap::class.java)
                return dataMap["version"] as String
            }
        }catch (e:Exception){
            e.printStackTrace()
            return "none"
        }
    }

    override fun updateVersion(version: String,response: HttpServletResponse):String {
        val nVersion = getVersion(version)
        if(nVersion==version) return "版本一致，不需要更新"
        val path = "D:\\Test\\"
        val zip = FileZip()
        val fileName = "patch-$nVersion.zip"
        zip.zipByFolder(path+"test",path+"patch\\"+fileName)
        FileUtils.downloadFile(fileName,path+"patch",response)
        return "版本不一致，已更新完毕"
    }
}

