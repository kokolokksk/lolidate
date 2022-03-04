package monster.loli.lolidate.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import monster.loli.lolidate.service.UpdateService
import monster.loli.lolidate.utils.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("update/catcatdm")
class UpdateController {
    @Autowired
    lateinit var updateService: UpdateService;

    @RequestMapping("getCatCatDMVersion")
    public fun getCatCatDMVersion(client_id: String?): String {
        if(client_id==null) return "none"
        return updateService.getVersion(client_id)
    }

    @RequestMapping("updateVersion")
    public fun updateVersion(request: HttpServletRequest,response: HttpServletResponse,version: String?): String {
        if(version==null) return "未传版本，不需要更新"
        return updateService.updateVersion(version,response)
    }

    @RequestMapping("downloadFile")
    public fun downloadFile(request: HttpServletRequest,respoense: HttpServletResponse,name: String?): String {
        FileUtils.downloadFile("mkf.png","D:\\robot\\ruyi\\img\\",respoense)
        return "下载成功"
    }

    @RequestMapping("getPatchFile")
    fun getPatchFile(@RequestBody fileList :String,request: HttpServletRequest,response: HttpServletResponse){
        updateService.getPatchFile(response,fileList)
    }
    @RequestMapping("getLastFullFile")
    fun getLastFullFile(request: HttpServletRequest,response: HttpServletResponse){
        updateService.getLastFullFile(request,response)
    }
}