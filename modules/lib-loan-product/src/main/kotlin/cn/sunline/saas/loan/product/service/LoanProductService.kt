package cn.sunline.saas.loan.product.service

import cn.sunline.saas.document.template.exception.DocumentTemplateNotFoundException
import cn.sunline.saas.document.template.modules.db.DocumentTemplate
import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.service.FeeFeatureService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.TermType
import cn.sunline.saas.interest.model.db.InterestFeatureModality
import cn.sunline.saas.interest.model.db.OverdueInterestFeatureModality
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.loan.product.component.LoanProductConditionComponent
import cn.sunline.saas.loan.product.exception.LoanProductNotFoundException
import cn.sunline.saas.loan.product.model.ConditionType
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.*
import cn.sunline.saas.loan.product.repository.LoanProductRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.model.db.PrepaymentFeatureModality
import cn.sunline.saas.repayment.model.db.RepaymentFeatureModality
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import cn.sunline.saas.repayment.service.RepaymentFeatureService
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.transaction.Transactional

/**
 * @title: LoanProductService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:07
 */
@Service
class LoanProductService(private var loanProductRepos: LoanProductRepository) :
    BaseMultiTenantRepoService<LoanProduct, Long>(loanProductRepos) {

    @Autowired
    private lateinit var seq: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductCondition: LoanProductConditionComponent

    @Autowired
    private lateinit var interestProductFeatureService: InterestFeatureService

    @Autowired
    private lateinit var repaymentProductFeatureService: RepaymentFeatureService

    @Autowired
    private lateinit var feeProductFeatureService: FeeFeatureService

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    @Transactional
    fun register(loanProductData: DTOLoanProduct): DTOLoanProductView {
        val newProductId = seq.nextId()

        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach {
            val loanUploadConfigure =
                loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }
        val documentTemplateList = ArrayList<DocumentTemplate>()
        loanProductData.documentTemplateFeatures?.forEach {
            val documentTemplate =
                documentTemplateService.getOne(it)?: throw DocumentTemplateNotFoundException("Invalid document template")
            documentTemplateList.add(documentTemplate)
        }

        val loanProductAdd = LoanProduct(
            newProductId,
            loanProductData.identificationCode,
            loanProductData.name,
            loanProductData.version,
            loanProductData.description,
            loanProductData.loanProductType,
            loanProductData.loanPurpose,
            loanProductData.businessUnit.toLong(),
            loanUploadConfigureList,
            documentTemplateList)

        loanProductData.amountConfiguration.apply {
            loanProductAdd.configurationOptions.add(
                loanProductCondition.amountCondition(
                    newProductId, BigDecimal(this.maxValueRange), BigDecimal(this.minValueRange)
                )
            )
        }

        loanProductData.termConfiguration.apply {
            loanProductAdd.configurationOptions.add(
                loanProductCondition.termCondition(
                    newProductId, this.maxValueRange, this.minValueRange
                )
            )
        }

        val interestFeature = loanProductData.interestFeature.run {
            interestProductFeatureService.register(
                newProductId,
                objectMapper.convertValue(this)
            )
        }

        val repaymentFeature = loanProductData.repaymentFeature.run {
            repaymentProductFeatureService.register(
                newProductId,
                objectMapper.convertValue(this)
            )
        }

        val feeFeatures = loanProductData.feeFeatures?.run {
            if (this.size > 0) {
                feeProductFeatureService.register(
                    newProductId,
                    objectMapper.convertValue(this)
                )
            } else {
                mutableListOf()
            }
        }

        val loanProduct = loanProductRepos.save(loanProductAdd)
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(loanProduct)
        setConfigurationOptions(loanProduct, dtoLoanProduct)

        dtoLoanProduct.interestFeature = objectMapper.convertValue(interestFeature)

        dtoLoanProduct.repaymentFeature =
            repaymentFeature.run { objectMapper.convertValue<DTORepaymentFeatureView>(this) }
        dtoLoanProduct.feeFeatures = feeFeatures?.run { objectMapper.convertValue(this) }

        return dtoLoanProduct
    }

    fun getLoanProduct(id: Long): DTOLoanProductView {
        val loanProduct = this.getOne(id) ?: throw LoanProductNotFoundException(
            "Invalid loan product",
            ManagementExceptionCode.PRODUCT_NOT_FOUND
        )
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(loanProduct)
        setConfigurationOptions(loanProduct, dtoLoanProduct)

        val interestFeature = interestProductFeatureService.findByProductId(id)
        val repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        val feeFeatures = feeProductFeatureService.findByProductId(id)

        dtoLoanProduct.interestFeature = interestFeature?.let { objectMapper.convertValue<DTOInterestFeatureView>(it) }!!
        dtoLoanProduct.repaymentFeature = repaymentFeature?.let { objectMapper.convertValue<DTORepaymentFeatureView>(it) }!!
        dtoLoanProduct.feeFeatures = feeFeatures?.let { objectMapper.convertValue<MutableList<DTOFeeFeatureView>>(it) }
        return dtoLoanProduct
    }

    fun getLoanProductPaged(
        name: String?,
        loanProductType: LoanProductType?,
        loanPurpose: String?,
        status:BankingProductStatus?,
        pageable: Pageable
    ): Page<LoanProduct> {

        val newPageable = if(pageable.isUnpaged){
            Pageable.unpaged()
        } else{
            PageRequest.of(pageable.pageNumber,pageable.pageSize, Sort.by(Sort.Order.desc("id")))
        }

        val page = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.notEqual(
                    root.get<BankingProductStatus>("status"),
                    BankingProductStatus.OBSOLETE
                )
            )
            name?.run { predicates.add(criteriaBuilder.like(root.get("name"), "$name%")) }
            loanProductType?.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<LoanProductType>("loanProductType"),
                        loanProductType
                    )
                )
            }
            loanPurpose?.run { predicates.add(criteriaBuilder.like(root.get("loanPurpose"), "$loanPurpose%")) }
            status?.run { predicates.add(criteriaBuilder.equal(root.get<BankingProductStatus>("status"), status)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, newPageable)
        val newProduct = getMaxVersionProductList(page)

        return rePaged(newProduct, pageable)
    }


    @Transactional
    fun updateLoanProductStatus(id: Long, status: BankingProductStatus): LoanProduct {
        val nowProduct = this.getOne(id) ?: throw LoanProductNotFoundException(
            "Invalid loan product",
            ManagementExceptionCode.PRODUCT_NOT_FOUND
        )
        if (status == BankingProductStatus.SOLD) {
            val productList = loanProductRepos.findByIdentificationCode(nowProduct.identificationCode)
                ?: throw LoanProductNotFoundException("Invalid loan product", ManagementExceptionCode.PRODUCT_NOT_FOUND)
            for (product in productList) {
                if (BankingProductStatus.SOLD == product.status) {
                    product.status = BankingProductStatus.OBSOLETE
                    save(product)
                }
            }
        }
        nowProduct.status = status
        return save(nowProduct)
    }

    fun findByIdentificationCode(identificationCode: String): MutableList<DTOLoanProductView> {
        val productList = loanProductRepos.findByIdentificationCode(identificationCode)
            ?: throw LoanProductNotFoundException("Invalid loan product", ManagementExceptionCode.PRODUCT_NOT_FOUND)
        val list = ArrayList<DTOLoanProductView>()
        for (product in productList) {
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(product)
            setConfigurationOptions(product, dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
    }

    fun findByIdentificationCodeAndStatus(
        identificationCode: String,
        bankingProductStatus: BankingProductStatus
    ): MutableList<DTOLoanProductView> {
        val productList = loanProductRepos.findByIdentificationCodeAndStatus(identificationCode, bankingProductStatus)
            ?: throw LoanProductNotFoundException("Invalid loan product", ManagementExceptionCode.PRODUCT_NOT_FOUND)
        val list = ArrayList<DTOLoanProductView>()
        for (product in productList) {
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(product)
            setConfigurationOptions(product, dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
    }

    fun getLoanProductListByStatus(bankingProductStatus: String, pageable: Pageable): Page<LoanProduct> {

        val page = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.notEqual(
                    root.get<BankingProductStatus>("status"),
                    bankingProductStatus
                )
            )
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())

        val newProduct = getMaxVersionProductList(page)

        return rePaged(newProduct, pageable)
    }

    @Transactional
    fun updateLoanProduct(id: Long, loanProductData: DTOLoanProduct): DTOLoanProductView {
        val oldLoanProduct = this.getOne(id) ?: throw Exception("Invalid loan product")

        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach {
            val loanUploadConfigure =
                loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }
        oldLoanProduct.loanUploadConfigureFeatures = loanUploadConfigureList

        val documentTemplateList = mutableListOf<DocumentTemplate>()
        loanProductData.documentTemplateFeatures?.forEach {
            val documentTemplate =
                documentTemplateService.getOne(it)?: throw DocumentTemplateNotFoundException("Invalid document template")
            documentTemplateList.add(documentTemplate)
        }

        oldLoanProduct.documentTemplateFeatures = documentTemplateList

        oldLoanProduct.businessUnit = loanProductData.businessUnit.toLong()
        oldLoanProduct.description = loanProductData.description
        oldLoanProduct.loanPurpose = loanProductData.loanPurpose

        //update loan product
        loanProductData.amountConfiguration.apply {
            oldLoanProduct.configurationOptions.forEach {
                if (this.id?.toLong() == it.id) {
                    it.setValue(BigDecimal(this.maxValueRange), BigDecimal(this.minValueRange))
                }
            }
        }

        loanProductData.termConfiguration.apply {
            oldLoanProduct.configurationOptions.forEach {
                if (this.id?.toLong() == it.id) {
                    it.setValue(
                        this.maxValueRange.term.num.let { it1 -> BigDecimal(it1) },
                        this.minValueRange.term.num.let { it1 -> BigDecimal(it1) }
                    )
                }
            }
        }

        val updateLoanProduct = this.save(oldLoanProduct)
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(updateLoanProduct)

        updateLoanProduct.configurationOptions.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration =
                        objectMapper.convertValue(it)
                    dtoLoanProduct.amountConfiguration.run {
                        this?.maxValueRange = it.getMaxValueRange().toString()
                        this?.minValueRange = it.getMinValueRange().toString()
                    }
                }
                ConditionType.TERM -> {
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(
                        it.id.toString(),
                        getValueRange(it.getMaxValueRange()),
                        getValueRange(it.getMinValueRange())
                    )
                }
            }
        }

        //update interestFeature
        var interestFeature = interestProductFeatureService.findByProductId(id)
        if (interestFeature == null) {
            val interestFeatureData = objectMapper.convertValue<DTOInterestFeatureAdd>(loanProductData.interestFeature)
            interestFeature = loanProductData.interestFeature.run {
                interestProductFeatureService.register(
                    id,
                    interestFeatureData
                )
            }
            dtoLoanProduct.interestFeature = objectMapper.convertValue(interestFeature)
        } else {
            loanProductData.interestFeature.run {
                interestFeature.ratePlanId = this.ratePlanId.toLong()
                interestFeature.interest =
                    InterestFeatureModality(interestFeature.id, this.baseYearDays, this.adjustFrequency)
                interestFeature.overdueInterest =
                    OverdueInterestFeatureModality(interestFeature.id, this.overdueInterestRatePercentage.toLong())
                interestFeature.interestType = this.interestType
                val updateInterestFeature = interestProductFeatureService.save(interestFeature)
                dtoLoanProduct.interestFeature = objectMapper.convertValue(updateInterestFeature)
            }
        }


        //update repaymentFeature
        val repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        if (repaymentFeature == null) {

            val repaymentFeatureData = loanProductData.repaymentFeature.let { objectMapper.convertValue<DTORepaymentFeatureAdd>(it) }
            val repaymentFeatureResult = repaymentProductFeatureService.register(
                id,
                repaymentFeatureData
            )
            dtoLoanProduct.repaymentFeature =  objectMapper.convertValue<DTORepaymentFeatureView>(repaymentFeatureResult)

        } else {
            loanProductData.repaymentFeature.run {
                repaymentFeature.payment = RepaymentFeatureModality(
                    repaymentFeature.id,
                    this.paymentMethod,
                    this.frequency,
                    this.repaymentDayType,
                    this.graceDays
                )
                repaymentFeature.prepayment.clear()
                this.prepaymentFeatureModality.forEach {
                    repaymentFeature.prepayment.add(
                        PrepaymentFeatureModality(
                            it.id?.toLong() ?: seq.nextId(),
                            it.term,
                            it.type,
                            it.penaltyRatio
                        )
                    )
                }
                val updateRepaymentFeature = repaymentProductFeatureService.save(repaymentFeature)
                dtoLoanProduct.repaymentFeature =
                    objectMapper.convertValue<DTORepaymentFeatureView>(updateRepaymentFeature)
            }
        }

        //update feeFeatures
        loanProductData.feeFeatures?.run {
            val feeFeatures = ArrayList<FeeFeature>()
            this.forEach {
                feeFeatures.add(
                    FeeFeature(
                        it.id?.toLong() ?: seq.nextId(),
                        id,
                        it.feeType,
                        it.feeMethodType,
                        it.feeAmount,
                        it.feeRate,
                        it.feeDeductType
                    )
                )
            }
            val updateFeeFeatures = feeProductFeatureService.save(feeFeatures).toMutableList()
            dtoLoanProduct.feeFeatures = objectMapper.convertValue<MutableList<DTOFeeFeatureView>>(updateFeeFeatures)
        }

        return dtoLoanProduct
    }

    fun getLoanProductLoanUploadConfigureMapping(loanUploadConfigureId: Long): Long {

        val page = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            val connection = root.join<LoanProduct, LoanUploadConfigure>("loanUploadConfigureFeatures", JoinType.INNER)
            predicates.add(criteriaBuilder.equal(connection.get<String>("id"), loanUploadConfigureId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())

        return page.totalElements
    }


    fun setConfigurationOptions(product: LoanProduct, dtoLoanProduct: DTOLoanProductView) {
        product.configurationOptions.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration =
                        objectMapper.convertValue(it)
                    dtoLoanProduct.amountConfiguration.run {
                        this?.maxValueRange = it.getMaxValueRange().toPlainString()
                        this?.minValueRange = it.getMinValueRange().toPlainString()
                    }
                }
                ConditionType.TERM -> {
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(
                        it.id.toString(),
                        getValueRange(it.getMaxValueRange()),
                        getValueRange(it.getMinValueRange())
                    )
                }
            }
        }
    }

    private fun getValueRange(value: BigDecimal): LoanTermType? {
        return LoanTermType.values().singleOrNull { it.term == TermType(value.toInt()) }
    }


    private fun getMaxVersionProductList(page: Page<LoanProduct>): List<LoanProduct> {
        val map = mutableMapOf<String, String>()
        page.forEach {
            val key = map[it.identificationCode]
            key?.run {
                if (it.version > key) {
                    map[it.identificationCode] = it.version
                }
            } ?: run {
                map[it.identificationCode] = it.version
            }
        }

        val newProduct = mutableListOf<LoanProduct>()

        map.forEach { (t, u) ->
            page.content.forEach {
                if (it.identificationCode == t && it.version == u) {
                    newProduct.add(it)
                }
            }
        }

        return newProduct
    }
}