package monster.loli.lolidate.service.impl

import com.google.gson.Gson
import monster.loli.lolidate.service.UpdateService
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.net.Proxy


@Service
class UpdateServiceImpl:UpdateService {
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

    override fun getVersion2(client: String): String {
        TODO("Not yet implemented")
    }

}

