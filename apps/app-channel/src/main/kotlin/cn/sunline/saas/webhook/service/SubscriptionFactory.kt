package cn.sunline.saas.webhook.service

import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import cn.sunline.saas.runner.AppCommandRunner
import cn.sunline.saas.channel.statistics.services.ApiDetailService
import cn.sunline.saas.channel.statistics.services.BusinessDetailService
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.pdpa.services.InitPdpa
import cn.sunline.saas.pdpa.services.PdpaService
import cn.sunline.saas.webhook.enum.WebhookType
import cn.sunline.saas.webhook.enum.WebhookType.*
import cn.sunline.saas.webhook.service.impl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class SubscriptionFactory {
    @Autowired
    private lateinit var tenantService: TenantService

    @Autowired
    private lateinit var appCommandRunner: AppCommandRunner

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @Autowired
    private lateinit var obsApi: ObsApi


    fun instance(webhookType: WebhookType):Subscription{
        return when(webhookType){
            SUBSCRIPTION_ONBOARD -> SubscriptionOnBoard(tenantService, appCommandRunner)
            SUBSCRIPTION_ADD -> SubscriptionAdd(tenantService, appCommandRunner)
            SUBSCRIPTION_REMOVE -> SubscriptionRemove(tenantService)
            SUBSCRIPTION_GET_INFO -> SubscriptionGetInfo(tenantService,obsApi)
            BILLING_GET_INFO -> BillingGetInfo(tenantService, tenantDateTime, apiDetailService, businessDetailService, customerDetailService)
        }
    }
}