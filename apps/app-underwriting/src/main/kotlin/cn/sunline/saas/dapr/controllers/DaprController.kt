package cn.sunline.saas.dapr.controllers

import cn.sunline.saas.dapr_wrapper.service.DaprService
import cn.sunline.saas.dapr_wrapper.service.dto.AppType
import cn.sunline.saas.dapr_wrapper.service.dto.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("dapr")
class DaprController {

    @Autowired
    private lateinit var daprService: DaprService


    @GetMapping("/subscribe")
    fun subscribe():String{
        return daprService.subscribe(AppType.UNDERWRITING)

    }

}