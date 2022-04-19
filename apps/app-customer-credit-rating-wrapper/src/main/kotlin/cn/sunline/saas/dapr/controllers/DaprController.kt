package cn.sunline.saas.dapr.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import cn.sunline.saas.dapr_wrapper.service.DaprService
import cn.sunline.saas.dapr_wrapper.service.dto.AppType
import org.springframework.beans.factory.annotation.Autowired

@RestController
@RequestMapping("dapr")
class DaprController {

    @Autowired
    private lateinit var daprService: DaprService

    @GetMapping("/subscribe")
    fun subscribe():String{
        return daprService.subscribe(AppType.CUSTOMER_CREDIT_RATING)
    }
}