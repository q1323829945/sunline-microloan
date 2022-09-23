package cn.sunline.saas.webhook.controller

import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.enum.WebhookType
import cn.sunline.saas.webhook.enum.WebhookType.*
import cn.sunline.saas.webhook.service.WebhookService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.UUID
import javax.websocket.server.PathParam

@RestController
@RequestMapping("webhook")
class WebhookController {
    @Autowired
    private lateinit var webhookService: WebhookService

    @PostMapping
    fun subscribeTenant(@PathParam(value = "type")type: WebhookType, @RequestBody dtoTenant: DTOWebhookRequest):WebhookResponse{

        val webhookResponse = when(type){
            SUBSCRIPTION_ONBOARD -> webhookService.saveTenant(dtoTenant)
            SUBSCRIPTION_ADD -> webhookService.saveTenant(dtoTenant)
            SUBSCRIPTION_REMOVE -> webhookService.disEnableTenant(dtoTenant)
            SUBSCRIPTION_GET_INFO -> webhookService.getInfo(dtoTenant)
            BILLING_GET_INFO -> webhookService.billing(dtoTenant)
        }

        return webhookResponse
    }

    @GetMapping
    fun uuid():String{
        return UUID.randomUUID().toString()
    }

}