package monster.loli.lolidate.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface UpdateService {
    fun getVersion(client:String):String
    fun updateVersion(version:String, response: HttpServletResponse):String
    fun getPatchFile(response: HttpServletResponse, fileList: String)
    fun getLastFullFile(request: HttpServletRequest, response: HttpServletResponse)
}