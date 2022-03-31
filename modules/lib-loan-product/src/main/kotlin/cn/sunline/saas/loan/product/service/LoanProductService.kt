package cn.sunline.saas.loan.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.service.FeeFeatureService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.db.InterestFeatureModality
import cn.sunline.saas.interest.model.db.OverdueInterestFeatureModality
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
import cn.sunline.saas.repayment.service.RepaymentFeatureService
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate
import javax.transaction.Transactional

/**
 * @title: LoanProductService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:07
 */
@Service
class LoanProductService(private var loanProductRepos:LoanProductRepository) :
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

    @Transactional
    fun register(loanProductData: DTOLoanProductAdd): DTOLoanProductView {
        val newProductId = seq.nextId()
        val loanProductAdd = LoanProduct(
            newProductId,
            loanProductData.identificationCode,
            loanProductData.name,
            loanProductData.version,
            loanProductData.description,
            loanProductData.loanProductType,
            loanProductData.loanPurpose
        )

        loanProductData.amountConfiguration?.apply {
            loanProductAdd.configurationOptions?.add(
                loanProductCondition.amountCondition(
                        newProductId, BigDecimal(this.maxValueRange), BigDecimal(this.minValueRange)
                )
            )
        }

        loanProductData.termConfiguration?.apply {
            loanProductAdd.configurationOptions?.add(
                loanProductCondition.termCondition(
                        newProductId,this.maxValueRange, this.minValueRange
                )
            )
        }

        val interestFeature = loanProductData.interestFeature?.run {
            interestProductFeatureService.register(
                newProductId,
                loanProductData.interestFeature
            )
        }
        val repaymentFeature = loanProductData.repaymentFeature?.run {
            repaymentProductFeatureService.register(
                newProductId,
                loanProductData.repaymentFeature
            )
        }

        val feeFeatures = loanProductData.feeFeatures?.run {
            feeProductFeatureService.register(
                newProductId,
                loanProductData.feeFeatures
            )
        }

        val loanProduct = loanProductRepos.save(loanProductAdd)

        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(loanProduct)

        setConfigurationOptions(loanProduct,dtoLoanProduct)

        dtoLoanProduct.interestFeature = interestFeature
        dtoLoanProduct.repaymentFeature = repaymentFeature
        dtoLoanProduct.feeFeatures = feeFeatures

        return dtoLoanProduct
    }


    @Transactional
    fun updateLoanProduct(id:Long,loanProductData: DTOLoanProductChange): DTOLoanProductView {
        val oldLoanProduct = this.getOne(id)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        //update loan product
        oldLoanProduct.name = loanProductData.name
        oldLoanProduct.description = loanProductData.description
        oldLoanProduct.loanPurpose = loanProductData.loanPurpose


        loanProductData.amountConfiguration?.apply {
            oldLoanProduct.configurationOptions?.forEach {
                if(this.id == it.id){
                    it.setValue(BigDecimal(this.maxValueRange), BigDecimal(this.minValueRange))
                }
            }
        }

        loanProductData.termConfiguration?.apply {
            oldLoanProduct.configurationOptions?.forEach {
                if(this.id == it.id){
                    it.description = loanProductCondition.updateTermCondition(this.id,this.maxValueRange,this.minValueRange,id)
                }
            }
        }

        val updateLoanProduct = this.save(oldLoanProduct)

        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(updateLoanProduct)

        setConfigurationOptions(updateLoanProduct,dtoLoanProduct)

        //update interestFeature
        var interestFeature = interestProductFeatureService.findByProductId(id)
        if(interestFeature == null){
            interestFeature = loanProductData.interestFeature?.run {
                interestProductFeatureService.register(
                        id,
                        objectMapper.convertValue(loanProductData.interestFeature)
                )
            }
            dtoLoanProduct.interestFeature = interestFeature
        } else {
            loanProductData.interestFeature?.run {
                interestFeature.ratePlanId = this.ratePlanId
                interestFeature.interest = InterestFeatureModality(interestFeature.id,this.baseYearDays,this.adjustFrequency)
                interestFeature.overdueInterest = OverdueInterestFeatureModality(interestFeature.id,this.overdueInterestRatePercentage)

                val updateInterestFeature = interestProductFeatureService.save(interestFeature)
                dtoLoanProduct.interestFeature = updateInterestFeature
            }
        }


        //update repaymentFeature
        var repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        if(repaymentFeature == null){
            repaymentFeature = loanProductData.repaymentFeature?.run {
                repaymentProductFeatureService.register(
                        id,
                        objectMapper.convertValue(loanProductData.repaymentFeature)
                )
            }
            dtoLoanProduct.repaymentFeature = repaymentFeature
        } else {
            loanProductData.repaymentFeature?.run {
                repaymentFeature.payment = RepaymentFeatureModality(repaymentFeature.id,this.paymentMethod,this.frequency,this.repaymentDayType)
                repaymentFeature.prepayment.clear()
                this.prepaymentFeatureModality.forEach {
                    repaymentFeature.prepayment.add(
                            PrepaymentFeatureModality(
                                    it.id?:seq.nextId(),
                                    it.term,
                                    it.type,
                                    it.penaltyRatio ?: BigDecimal.ZERO
                            )
                    )
                }
                val updateRepaymentFeature = repaymentProductFeatureService.save(repaymentFeature)
                dtoLoanProduct.repaymentFeature = updateRepaymentFeature
            }
        }

        //update feeFeatures
        loanProductData.feeFeatures?.run {
            val feeFeatures = ArrayList<FeeFeature>()
            this.forEach {
                feeFeatures.add(
                        FeeFeature(
                                it.id?:seq.nextId(),
                                id,
                                it.feeType,
                                it.feeMethodType,
                                BigDecimal(it.feeAmount),
                                BigDecimal(it.feeRate),
                                it.feeDeductType
                        )
                )
            }
            val updateFeeFeatures = feeProductFeatureService.save(feeFeatures).toMutableList()
            dtoLoanProduct.feeFeatures = updateFeeFeatures
        }

        return dtoLoanProduct
    }


    fun getLoanProduct(id:Long): DTOLoanProductView {
        val loanProduct = this.getOne(id)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(loanProduct)

        setConfigurationOptions(loanProduct,dtoLoanProduct)

        val interestFeature = interestProductFeatureService.findByProductId(id)
        val repaymentFeature = repaymentProductFeatureService.findByProductId(id)
        val feeFeatures = feeProductFeatureService.findByProductId(id)
        dtoLoanProduct.interestFeature = interestFeature
        dtoLoanProduct.repaymentFeature = repaymentFeature
        dtoLoanProduct.feeFeatures = feeFeatures

        return dtoLoanProduct
    }

    fun getLoanProductPaged(name:String?,
                            loanProductType: LoanProductType?,
                            loanPurpose: String?,
                            pageable: Pageable): Page<LoanProduct> {

        val paged = this.getPaged({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            name?.run { predicates.add(criteriaBuilder.like(root.get("name"),"$name%")) }
            loanProductType?.run { predicates.add(criteriaBuilder.equal(root.get<LoanProductType>("loanProductType"),loanProductType)) }
            loanPurpose?.run { predicates.add(criteriaBuilder.equal(root.get<String>("loanPurpose"),loanPurpose)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable)

        return paged
    }

    fun updateLoanProductStatus(id: Long, status: BankingProductStatus): LoanProduct {
        val product = this.getOne(id) ?: throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        product.status = status
        return save(product)
    }


    fun findByIdentificationCode(identificationCode:String):DTOLoanProductView{
        val product = loanProductRepos.findByIdentificationCode(identificationCode)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)

        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(product)

        setConfigurationOptions(product,dtoLoanProduct)

        return dtoLoanProduct
    }

    private fun setConfigurationOptions(product:LoanProduct,dtoLoanProduct:DTOLoanProductView){
        product.configurationOptions?.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration = objectMapper.convertValue<DTOAmountLoanProductConfigurationView>(it)
                    dtoLoanProduct.amountConfiguration?.run {
                        this.maxValueRange = it.getMaxValueRange().toPlainString()
                        this.minValueRange = it.getMinValueRange().toPlainString()
                    }
                }
                ConditionType.TERM ->{
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id,getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
                }
            }
        }
    }

    private fun getValueRange(value:BigDecimal): LoanTermType? {
        for(type in LoanTermType.values()){
            if(value.toInt() == type.days){
                return type
            }
        }

        return null
    }

}