package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.TransactionType
import cn.sunline.saas.channel.statistics.services.ApiDetailService
import cn.sunline.saas.channel.statistics.services.BusinessDetailService
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Pageable
import java.util.*
import javax.persistence.criteria.Predicate

class BillingGetInfo(
    private val tenantService: TenantService,
    private val tenantDateTime: TenantDateTime,
    private val apiDetailService: ApiDetailService,
    private val businessDetailService: BusinessDetailService,
    private val  customerDetailService: CustomerDetailService
): Subscription {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val tenant = tenantService.findByUUID(UUID.fromString(dtoWebhookRequest.tenant))
        tenant?: run {
            return WebhookResponse(
                false,
                mutableMapOf(
                    "msg" to "tenant is not found!!!",
                )
            )
        }
        ContextUtil.setTenant(tenant.id.toString())

        val year = dtoWebhookRequest.data["BILLING_CYCLE_YEAR_KEY"]?.run { this.toInt() }
        val month = dtoWebhookRequest.data["BILLING_CYCLE_MONTH_KEY"]?.run { this.toInt() }
        val day = dtoWebhookRequest.data["BILLING_CYCLE_DAY_KEY"]?.run { this.toInt() }
        var api = 0
        var business = 0
        var customer = 0
        if(year != null && month != null && day != null){
            api = getApiStatisticsByDate(year,month,day)
            business = getBusinessStatisticsByDate(year,month,day)
            customer = getCustomerStatisticsByDate(year,month,day,)
        }

        return WebhookResponse(
            true,
            mutableMapOf(
                "BILLING_API_VOLUME_KEY" to "$api",
                "BILLING_USER_VOLUME_KEY" to "$customer",
                "BILLING_TRANSACTION_VOLUME_KEY" to "$business"
            )
        )
    }


    private fun getApiStatisticsByDate(year:Int,month:Int,day:Int): Int {
        val startDate = tenantDateTime.toTenantDateTime(year, month, day)
        val endDate = startDate.plusDays(1)
        val page = apiDetailService.getPageWithTenant({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),startDate.toDate(),endDate.toDate()))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, Pageable.unpaged())

        return page.content.size

    }

    private fun getBusinessStatisticsByDate(year:Int, month:Int, day:Int): Int {
        val startDate = tenantDateTime.toTenantDateTime(year, month, day)
        val endDate = startDate.plusDays(1)
        val page = businessDetailService.getPageWithTenant({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),startDate.toDate(),endDate.toDate()))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, Pageable.unpaged())
        return page.content.filter { it.transactionType == TransactionType.PAYMENT }.size
    }

    private fun getCustomerStatisticsByDate(year:Int, month:Int, day:Int): Int {
        val startDate = tenantDateTime.toTenantDateTime(year, month, day)
        val endDate = startDate.plusDays(1)
        val page = customerDetailService.getPageWithTenant({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),startDate.toDate(),endDate.toDate()))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, Pageable.unpaged())

        return page.content.filter { it.partyType == PartyType.PERSON }.size
    }
}