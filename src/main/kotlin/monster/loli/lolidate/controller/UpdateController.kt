package monster.loli.lolidate.controller

import monster.loli.lolidate.service.UpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("update")
class UpdateController {
    @Autowired
    lateinit var updateService: UpdateService;

    @RequestMapping("getCatCatDMVersion")
    public fun getCatCatDMVersion(client_id: String): String {
        return updateService.getVersion(client_id)
    }
}