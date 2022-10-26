package cn.sunline.saas.controller

import cn.sunline.saas.context.GatewayContext
import cn.sunline.saas.modules.dto.TenantInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestController {
    @Autowired
    private lateinit var gatewayContext: GatewayContext

    @PostMapping
    fun ok(){
        println("OK")
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id:String):TenantInstance?{
        return gatewayContext.get(id)
    }

    @GetMapping
    fun getContext():List<TenantInstance>{
        return gatewayContext.getAll()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id:String){
        return gatewayContext.remove(id)
    }
}