package cn.sunline.saas.test.controller

import cn.sunline.saas.dapr_wrapper.bindings.BindingsService
import cn.sunline.saas.dapr_wrapper.constant.CHANNEL_SYNC_BINDINGS
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.bindings.BindingsOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestController {
    @PostMapping("rabbitMQ")
    fun setRabbitMQ(@RequestBody message:String){
        BindingsService.bindings(
            bindingsName = CHANNEL_SYNC_BINDINGS,
            operation = BindingsOperation.CREATE.name.lowercase(),
            payload = message,
            tenant = ContextUtil.getTenant()
        )
    }
}