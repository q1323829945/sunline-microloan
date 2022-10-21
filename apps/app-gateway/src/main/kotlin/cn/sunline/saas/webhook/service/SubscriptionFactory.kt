package cn.sunline.saas.webhook.service

import cn.sunline.saas.service.GatewayService
import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.service.StatisticsService
import cn.sunline.saas.webhook.enum.WebhookType
import cn.sunline.saas.webhook.enum.WebhookType.*
import cn.sunline.saas.webhook.service.impl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SubscriptionFactory {
    @Autowired
    private lateinit var instanceService: InstanceService
    @Autowired
    private lateinit var gatewayService: GatewayService
    @Autowired
    private lateinit var statisticsService: StatisticsService

    fun instance(webhookType: WebhookType):Subscription{
        return when(webhookType){
            SUBSCRIPTION_ONBOARD -> SubscriptionOnBoard(instanceService,gatewayService)
            SUBSCRIPTION_ADD -> SubscriptionAdd(instanceService,gatewayService)
            SUBSCRIPTION_REMOVE -> SubscriptionRemove(instanceService,gatewayService)
            SUBSCRIPTION_GET_INFO -> SubscriptionGetInfo(instanceService)
            BILLING_GET_INFO -> BillingGetInfo(instanceService,statisticsService)
        }
    }
}