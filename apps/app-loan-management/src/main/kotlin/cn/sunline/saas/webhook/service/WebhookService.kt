package cn.sunline.saas.webhook.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import cn.sunline.saas.runner.AppCommandRunner
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOCustomerStatisticsCount
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.TransactionType
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetailQueryParams
import cn.sunline.saas.statistics.services.*
import cn.sunline.saas.webhook.dto.WebhookResponse
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import org.springframework.data.domain.Pageable
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class WebhookService(
    private val sequence: Sequence
) {
    @Autowired
    private lateinit var tenantService: TenantService

    @Autowired
    private lateinit var appCommandRunner: AppCommandRunner

    @Autowired
    private lateinit var pdpaAuthorityService: PdpaAuthorityService

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun saveTenant(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        val tenant = tenantService.findBySaasUUID(UUID.fromString(dtoWebhookRequest.tenant))
        return if(tenant == null){
            addTenant(dtoWebhookRequest)
        } else {
            enableTenant(tenant)
        }
    }

    fun addTenant(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        val country = dtoWebhookRequest.tenantInfo["country"]?.run {
            CountryType.values().first { it.countryName.contains(this) }
        }?: run {
            CountryType.CHN
        }

        val tenant = Tenant(
            name = dtoWebhookRequest.tenantInfo["entityName"]?: run { "default" },
            country = country,
            enabled = true,
            saasUUID = UUID.fromString(dtoWebhookRequest.tenant),
        )
        val saveOne = tenantService.save(tenant)

        ContextUtil.setTenant(saveOne.id.toString())
        appCommandRunner.run()
        pdpaAuthorityService.register()

        return WebhookResponse(
            true,
            mutableMapOf(
                "tenant" to saveOne.uuid.toString(),
                "SUBSCRIPTION_STATUS_KEY" to "true"
            )
        )
    }

    fun enableTenant(tenant: Tenant):WebhookResponse{
        tenant.enabled = true
        return updateTenant(tenant)
    }

    fun disEnableTenant(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val tenant = tenantService.findBySaasUUID(UUID.fromString(dtoWebhookRequest.tenant))
        tenant?.run {
            tenant.enabled = false
            return updateTenant(tenant)
        }

        return WebhookResponse(
            false,
            mutableMapOf(
                "msg" to "tenant is not found!!!",
                "SUBSCRIPTION_STATUS_KEY" to "false"
            )
        )
    }

    private fun updateTenant(tenant: Tenant):WebhookResponse{
        val updateOne = tenantService.save(tenant)

        return WebhookResponse(
            true,
            mutableMapOf(
                "tenant" to updateOne.uuid.toString(),
                "SUBSCRIPTION_STATUS_KEY" to "true"
            )
        )
    }

    fun getInfo(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        val tenant = tenantService.findBySaasUUID(UUID.fromString(dtoWebhookRequest.tenant))
        tenant?: run {
            return WebhookResponse(
                false,
                mutableMapOf(
                    "msg" to "tenant is not found!!!",
                )
            )
        }
        val api = "API"
        val apiDoc = "API_DOC"
        val management = "MANAGEMENT"
        val managementMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://quickloan-management-demo.saas.finline.app/",
            "username" to "admin",
            "password" to "admin",
            "client_id" to tenant.uuid.toString()
        ))
        val web = "WEB"
        val webMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://quickloan-app-demo.saas.finline.app?tenant=${tenant.uuid}",
            "access_key" to "300348",
            "client_id" to tenant.uuid.toString(),
        ))
        return WebhookResponse(
            true,
            mutableMapOf(
                "SUBSCRIPTION_META_KEY_1" to management,
                "${management}_DESCRIPTION" to "Access backend management portal",
                "${management}_TYPE" to "management",
                "${management}_NAME" to "Microloan Management Portal",
                "${management}_VALUE" to managementMap,

                "SUBSCRIPTION_META_KEY_2" to web,
                "${web}_DESCRIPTION" to "Integrate using client mobile web",
                "${web}_TYPE" to "client_web",
                "${web}_NAME" to "Microloan Mobile Web",
                "${web}_VALUE" to webMap,
            )
        )
    }

    fun billing(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        val tenant = tenantService.findBySaasUUID(UUID.fromString(dtoWebhookRequest.tenant))
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

    @Autowired
    private lateinit var apiDetailService: ApiDetailService


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

    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    fun getBusinessStatisticsByDate(year:Int,month:Int,day:Int): Int {
        val startDate = tenantDateTime.toTenantDateTime(year, month, day)
        val endDate = startDate.plusDays(1)
        val page = businessDetailService.getPageWithTenant({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),startDate.toDate(),endDate.toDate()))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, Pageable.unpaged())
        return page.content.filter { it.transactionType == TransactionType.PAYMENT }.size
    }

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    fun getCustomerStatisticsByDate(year:Int,month:Int,day:Int): Int {
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
