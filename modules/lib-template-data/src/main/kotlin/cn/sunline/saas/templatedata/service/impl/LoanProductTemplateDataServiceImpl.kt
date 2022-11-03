package cn.sunline.saas.templatedata.service.impl

import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.model.dto.*
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.organisation.service.OrganisationService
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.templatedata.exception.TemplateDataBusinessException
import cn.sunline.saas.templatedata.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

@Service
class LoanProductTemplateDataServiceImpl : TemplateDataService() {
    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    @Autowired
    private lateinit var organisationService: OrganisationService

    private fun getDTOLoanUploadConfigure(): List<Long> {
        val content = loanUploadConfigureService.getPaged(pageable = Pageable.unpaged()).content
        if (content.isEmpty()) {
            throw TemplateDataBusinessException("loan upload configure is empty")
        }
        val loanUploadConfigures = mutableListOf<Long>()
        content.forEach {
            loanUploadConfigures += it.id
        }
        return loanUploadConfigures
    }

    private fun getDTODocumentTemplate(): List<Long> {
        val content = documentTemplateService.getPageWithTenant(pageable = Pageable.unpaged()).content
        if (content.isEmpty()) {
            throw TemplateDataBusinessException("loan document template is empty")
        }
        val documentTemplates = mutableListOf<Long>()
        content.forEach {
            documentTemplates += it.id!!
        }
        return documentTemplates
    }

    private fun getDTOInterestFeature(): DTOInterestFeature {

        val content = ratePlanService.getPaged(pageable = Pageable.unpaged()).content
        if (content.isEmpty()) {
            throw TemplateDataBusinessException("rate plan is empty")
        }
        return if (content.none { it.type == RatePlanType.STANDARD }) {
            DTOInterestFeature(
                id = null,
                productId = null,
                interestType = InterestType.FIXED,
                ratePlanId = content.first().id.toString(),
                interest = DTOInterestFeatureModality(
                    id = null,
                    baseYearDays = BaseYearDays.ACCOUNT_YEAR,
                    adjustFrequency = "10",
                    floatPoint = null,
                    floatRatio = null,
                ),
                overdueInterest = DTOOverdueInterestFeatureModality(
                    id = null,
                    overdueInterestRatePercentage = "10"
                )
            )
        } else if (content.all { it.type == RatePlanType.STANDARD }) {
            DTOInterestFeature(
                id = null,
                productId = null,
                interestType = InterestType.FLOATING_RATE_NOTE,
                ratePlanId = content.first().id.toString(),
                interest = DTOInterestFeatureModality(
                    id = null,
                    baseYearDays = BaseYearDays.ACCOUNT_YEAR,
                    adjustFrequency = "10",
                    floatPoint = BigDecimal("0.2"),
                    floatRatio = BigDecimal("0.2"),
                ),
                overdueInterest = DTOOverdueInterestFeatureModality(
                    id = null,
                    overdueInterestRatePercentage = "10"
                )
            )
        } else {
            DTOInterestFeature(
                id = null,
                productId = null,
                interestType = InterestType.FLOATING_RATE_NOTE,
                ratePlanId = content.first { it.type == RatePlanType.STANDARD }.id.toString(),
                interest = DTOInterestFeatureModality(
                    id = null,
                    baseYearDays = BaseYearDays.ACCOUNT_YEAR,
                    adjustFrequency = "10",
                    floatPoint = BigDecimal("0.2"),
                    floatRatio = BigDecimal("0.2"),
                ),
                overdueInterest = DTOOverdueInterestFeatureModality(
                    id = null,
                    overdueInterestRatePercentage = "10"
                )
            )
        }
    }

    private fun getDTORepaymentFeature(): DTORepaymentFeature {
        val dtoPrepaymentFeatureModalityMutableList = mutableListOf<DTOPrepaymentFeatureModality>()
        dtoPrepaymentFeatureModalityMutableList += DTOPrepaymentFeatureModality(
            id = null,
            term = LoanTermType.ONE_MONTH,
            type = PrepaymentType.FULL_REDEMPTION,
            penaltyRatio = BigDecimal("0.2")
        )
        return DTORepaymentFeature(
            id = null,
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            frequency = RepaymentFrequency.ONE_MONTH,
            repaymentDayType = RepaymentDayType.BASE_LOAN_DAY,
            prepaymentFeatureModality = dtoPrepaymentFeatureModalityMutableList,
            graceDays = 0
        )
    }

    private fun getDTOAmountLoanProductConfiguration(): DTOAmountLoanProductConfiguration {
        return DTOAmountLoanProductConfiguration(null, "1000000", "10000")
    }

    private fun getDTOTermLoanProductConfiguration(): DTOTermLoanProductConfiguration {
        return DTOTermLoanProductConfiguration(null, LoanTermType.THREE_YEAR, LoanTermType.ONE_MONTH)
    }

    private fun getBusinessUnit(): String {
        val content = organisationService.getPageWithTenant(null, Pageable.unpaged()).content
        if (content.isEmpty()) {
            throw TemplateDataBusinessException("organisation is empty")
        }
        return content.first().businessUnits.first().id.toString()
    }

    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()

        constructor.parameters.forEach { param ->
            if (!(param.name!! == "version" ||
                        param.name!! == "status" ||
                        param.name!! == "id" ||
                        param.name!! == "feeFeatures")
            ) {
                if (param.type.classifier == Long::class) {
                    mapData[param] = sequence.nextId()
                }
                if (param.type.classifier == String::class) {
                    if (param.name!!.contains("id") && param.name!! != "identificationCode") {
                        mapData[param] = sequence.nextId().toString()
                    } else {
                        mapData[param] = "product_" + sequence.nextId().toString().substring(6, 9)
                    }
                }
                if (param.type.classifier == Date::class) {
                    mapData[param] = tenantDateTime.now().toDate()
                }
                if (param.type.classifier == Boolean::class) {
                    mapData[param] = false
                }
                if (param.type.classifier == Boolean::class) {
                    mapData[param] = false
                }
                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
                if (param.type.classifier == DTOAmountLoanProductConfiguration::class) {
                    mapData[param] = getDTOAmountLoanProductConfiguration()
                }
                if (param.type.classifier == DTOTermLoanProductConfiguration::class) {
                    mapData[param] = getDTOTermLoanProductConfiguration()
                }
                if (param.type.classifier == DTOInterestFeature::class) {
                    mapData[param] = getDTOInterestFeature()
                }
                if (param.type.classifier == DTORepaymentFeature::class) {
                    mapData[param] = getDTORepaymentFeature()
                }
                if (param.type.classifier == String::class && param.name!!.contains("businessUnit")) {
                    mapData[param] = getBusinessUnit()
                }
                if (param.name!!.contains("loanUploadConfigureFeatures")) {
                    mapData[param] = getDTOLoanUploadConfigure()
                }
                if (param.name!!.contains("documentTemplateFeatures")) {
                    mapData[param] = getDTODocumentTemplate()
                }
            } else {
                if (param.name!! == "version") {
                    mapData[param] = "0"
                } else {
                    mapData[param] = null
                }
            }
        }

        return constructor.callBy(mapData)

    }
}