package monster.loli.lolidate.service.impl

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import jakarta.servlet.http.HttpServletResponse
import monster.loli.lolidate.dao.UpdateDao
import monster.loli.lolidate.service.UpdateService
import monster.loli.lolidate.utils.FileUtils
import monster.loli.lolidate.utils.FileZip
import monster.loli.lolidate.utils.LoveLoliiiUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy
import java.nio.file.Files


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

    @Autowired
    lateinit var updateDao: UpdateDao
    @Value("\${env.config.file_path}")
    lateinit var filePath:String
    @Value("\${env.config.file_list}")
    lateinit var fileList:String
    override fun getPatchFile(response: HttpServletResponse, fileList: String) {
        val fileListJ =  JsonParser.parseString(fileList).asJsonObject
        val clientVersion = JsonParser.parseString(fileList).asJsonObject["application"].asString
        // query last version
        val r = updateDao.findAllList()
        log.info(r.toString())
        val lastV :String = r!![r.size-1]!!.version
        val new = File(r!![r.size-1]!!.path).readText()
        val versionPatch = JsonParser.parseString(fileList).asJsonObject["application"].asString.replace(".","") +"-"+lastV.replace(".","")
        val patchFileName = "patch-$versionPatch.7z"
        val patchFile = File(filePath+"patch"+File.separator+patchFileName)
        if(patchFile.exists()){
            // down
            FileUtils.downloadFile(patchFileName,filePath+"patch"+File.separator,response)
        }else{
            val patchFileList = LoveLoliiiUtils.getPatchFileList(fileList,new)
            val sevenZFileList = ArrayList<File>()
            patchFileList.forEach {
                sevenZFileList.add(File(it["file_path"].toString()))
            }
            FileUtils.compressFileTo7z(sevenZFileList,patchFile.toPath(),"")
            //down
            FileUtils.downloadFile(patchFileName,filePath+"patch"+File.separator,response)
        }

    }
}

