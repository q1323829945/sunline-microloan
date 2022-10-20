package cn.sunline.saas.webhook.controller

import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.enum.WebhookType
import cn.sunline.saas.webhook.service.SubscriptionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("webhook")
class WebhookController {
    @Autowired
    private lateinit var subscriptionFactory: SubscriptionFactory

    @PostMapping
    fun subscribeTenant(@PathParam(value = "type")type: WebhookType, @RequestBody dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        return subscriptionFactory.instance(type).run(dtoWebhookRequest)
    }

    @GetMapping
    fun uuid():String{
        val uuid = UUID.randomUUID().toString()
        println(uuid)
        return uuid
    }

}