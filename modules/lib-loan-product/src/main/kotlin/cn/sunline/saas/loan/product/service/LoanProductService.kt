package cn.sunline.saas.loan.product.service

import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.fee.service.FeeFeatureService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
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
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root
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

    @Transactional
    fun register(loanProductData: DTOLoanProduct): DTOLoanProduct {
        val newProductId = seq.nextId()

        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach {
            val loanUploadConfigure =
                loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }

        val loanProductAdd = LoanProduct(
            newProductId,
            loanProductData.identificationCode,
            loanProductData.name,
            loanProductData.version,
            loanProductData.description,
            loanProductData.loanProductType,
            loanProductData.loanPurpose,
            loanUploadConfigureList
        )

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
            val interestFeatureData = objectMapper.convertValue<DTOInterestFeatureAdd>(loanProductData.interestFeature)
            interestProductFeatureService.register(
                newProductId,
                interestFeatureData
            )
        }

        val repaymentFeature = loanProductData.repaymentFeature?.run {
            val repaymentFeatureData =
                loanProductData.repaymentFeature?.let { objectMapper.convertValue<DTORepaymentFeatureAdd>(it) }
            if (repaymentFeatureData != null) {
                repaymentProductFeatureService.register(
                    newProductId,
                    repaymentFeatureData
                )
            }
        }

        val feeFeatures = loanProductData.feeFeatures?.run {
            val feeFeatureDataList =
                loanProductData.feeFeatures?.let { objectMapper.convertValue<MutableList<DTOFeeFeatureAdd>>(it) }
            if (feeFeatureDataList != null && feeFeatureDataList.size > 0) {
                feeProductFeatureService.register(
                    newProductId,
                    feeFeatureDataList
                )
            }
        }

        val loanProduct = loanProductRepos.save(loanProductAdd)
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProduct>(loanProduct)
        setConfigurationOptions(loanProduct, dtoLoanProduct)

        dtoLoanProduct.interestFeature = objectMapper.convertValue<DTOInterestFeature>(interestFeature)
        dtoLoanProduct.repaymentFeature = repaymentFeature?.let { objectMapper.convertValue<DTORepaymentFeature>(it) }
        dtoLoanProduct.feeFeatures = feeFeatures?.let { objectMapper.convertValue<MutableList<DTOFeeFeature>>(it) }

        return dtoLoanProduct
    }

    fun getLoanProduct(id: Long): DTOLoanProduct {
        val loanProduct = this.getOne(id) ?: throw LoanProductNotFoundException(
            "Invalid loan product",
            ManagementExceptionCode.PRODUCT_NOT_FOUND
        )
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProduct>(loanProduct)
        setConfigurationOptions(loanProduct, dtoLoanProduct)

        val interestFeature = interestProductFeatureService.findByProductId(id)
        if (interestFeature != null) {
            dtoLoanProduct.interestFeature = objectMapper.convertValue<DTOInterestFeature>(interestFeature)
        }
        val repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        val feeFeatures = feeProductFeatureService.findByProductId(id)
        dtoLoanProduct.repaymentFeature = repaymentFeature?.let { objectMapper.convertValue<DTORepaymentFeature>(it) }
        dtoLoanProduct.feeFeatures = feeFeatures?.let { objectMapper.convertValue<MutableList<DTOFeeFeature>>(it) }
        return dtoLoanProduct
    }

    fun getLoanProductPaged(
        name: String?,
        loanProductType: LoanProductType?,
        loanPurpose: String?,
        pageable: Pageable
    ): Page<LoanProduct> {

        return loanProductRepos.getLoanProductPaged(name, loanProductType?.name, loanPurpose, pageable)
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

    fun findByIdentificationCode(identificationCode: String): MutableList<DTOLoanProduct> {
        val productList = loanProductRepos.findByIdentificationCode(identificationCode)
            ?: throw LoanProductNotFoundException("Invalid loan product", ManagementExceptionCode.PRODUCT_NOT_FOUND)
        val list = ArrayList<DTOLoanProduct>()
        for (product in productList) {
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProduct>(product)
            setConfigurationOptions(product, dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
    }

    fun findByIdentificationCodeAndStatus(
        identificationCode: String,
        bankingProductStatus: BankingProductStatus
    ): MutableList<DTOLoanProduct> {
        val productList = loanProductRepos.findByIdentificationCodeAndStatus(identificationCode, bankingProductStatus)
            ?: throw LoanProductNotFoundException("Invalid loan product", ManagementExceptionCode.PRODUCT_NOT_FOUND)
        val list = ArrayList<DTOLoanProduct>()
        for (product in productList) {
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProduct>(product)
            setConfigurationOptions(product, dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
    }

    fun getLoanProductListByStatus(bankingProductStatus: String, pageable: Pageable): Page<LoanProduct> {
        return loanProductRepos.getLoanProductListByStatus(bankingProductStatus, pageable)
    }

    @Transactional
    fun updateLoanProduct(id: Long, loanProductData: DTOLoanProduct): DTOLoanProduct {
        val oldLoanProduct = this.getOne(id) ?: throw Exception("Invalid loan product")

        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach {
            val loanUploadConfigure =
                loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }
        oldLoanProduct.loanUploadConfigureFeatures = loanUploadConfigureList

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
                        this.maxValueRange.days.let { it1 -> BigDecimal(it1) },
                        this.minValueRange.days.let { it1 -> BigDecimal(it1) }
                    )
                }
            }
        }

        val updateLoanProduct = this.save(oldLoanProduct)

        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProduct>(updateLoanProduct)

        updateLoanProduct.configurationOptions.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration =
                        objectMapper.convertValue<DTOAmountLoanProductConfiguration>(it)
                    dtoLoanProduct.amountConfiguration.run {
                        this.maxValueRange = it.getMaxValueRange().toString()
                        this.minValueRange = it.getMinValueRange().toString()
                    }
                }
                ConditionType.TERM -> {
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfiguration(
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
            dtoLoanProduct.interestFeature = objectMapper.convertValue<DTOInterestFeature>(interestFeature)
        } else {
            loanProductData.interestFeature.run {
                interestFeature.ratePlanId = this.ratePlanId.toLong()
                interestFeature.interest =
                    InterestFeatureModality(interestFeature.id, this.baseYearDays, this.adjustFrequency)
                interestFeature.overdueInterest =
                    OverdueInterestFeatureModality(interestFeature.id, this.overdueInterestRatePercentage.toLong())
                interestFeature.interestType = this.interestType
                val updateInterestFeature = interestProductFeatureService.save(interestFeature)
                dtoLoanProduct.interestFeature = objectMapper.convertValue<DTOInterestFeature>(updateInterestFeature)
            }
        }


        //update repaymentFeature
        val repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        if (repaymentFeature == null) {

            val repaymentFeatureData =
                loanProductData.repaymentFeature?.let { objectMapper.convertValue<DTORepaymentFeatureAdd>(it) }
            if (repaymentFeatureData != null) {
                repaymentProductFeatureService.register(
                    id,
                    repaymentFeatureData
                )
            }
            dtoLoanProduct.repaymentFeature = repaymentFeature

        } else {
            loanProductData.repaymentFeature?.run {
                repaymentFeature.payment = RepaymentFeatureModality(
                    repaymentFeature.id,
                    this.paymentMethod,
                    this.frequency,
                    this.repaymentDayType
                )
                repaymentFeature.prepayment.clear()
                this.prepaymentFeatureModality.forEach {
                    repaymentFeature.prepayment.add(
                        PrepaymentFeatureModality(
                            (it.id ?: seq.nextId()) as Long,
                            it.term,
                            it.type,
                            it.penaltyRatio ?: BigDecimal.ZERO
                        )
                    )
                }
                val updateRepaymentFeature = repaymentProductFeatureService.save(repaymentFeature)
                dtoLoanProduct.repaymentFeature = objectMapper.convertValue<DTORepaymentFeature>(updateRepaymentFeature)
            }
        }

        //update feeFeatures
        loanProductData.feeFeatures?.run {
            val feeFeatures = ArrayList<FeeFeature>()
            this.forEach {
                feeFeatures.add(
                    FeeFeature(
                        (it.id ?: seq.nextId()) as Long,
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
            dtoLoanProduct.feeFeatures = objectMapper.convertValue<MutableList<DTOFeeFeature>>(updateFeeFeatures)
        }

        return dtoLoanProduct
    }

    fun getLoanProductLoanUploadConfigureMapping(loanUploadConfigureId: Long): Long {
        return loanProductRepos.getLoanProductLoanUploadConfigureMapping(loanUploadConfigureId)
    }


    private fun setConfigurationOptions(product: LoanProduct, dtoLoanProduct: DTOLoanProduct) {
        product.configurationOptions.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration =
                        objectMapper.convertValue<DTOAmountLoanProductConfiguration>(it)
                    dtoLoanProduct.amountConfiguration.run {
                        this.maxValueRange = it.getMaxValueRange().toPlainString()
                        this.minValueRange = it.getMinValueRange().toPlainString()
                    }
                }
                ConditionType.TERM -> {
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfiguration(
                        it.id.toString(),
                        getValueRange(it.getMaxValueRange()),
                        getValueRange(it.getMinValueRange())
                    )
                }
            }
        }
    }

    private fun getValueRange(value: BigDecimal): LoanTermType {
        return LoanTermType.values().single { it.days == value.toInt() }
    }
}