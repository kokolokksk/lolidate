package monster.loli.lolidate.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("update")
class UpdateController {
    @RequestMapping("getCatCatDMVersion")
    public fun getCatCatDMVersion(client_id:String):String{

        return "1.0.5"
    }
}