package cn.sunline.saas.controller

import cn.sunline.saas.context.GatewayContext
import cn.sunline.saas.modules.dto.TenantInstance
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("test")
class TestController {
    @PostMapping
    fun ok(){
        println("OK")
    }

    @GetMapping
    fun getContext():List<TenantInstance>{
        return GatewayContext.getAll()
    }

    @PostMapping("microloan")
    fun test():ModelAndView{
        return ModelAndView("")
    }
}