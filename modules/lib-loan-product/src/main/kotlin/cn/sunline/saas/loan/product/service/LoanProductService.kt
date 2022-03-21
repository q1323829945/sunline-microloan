package cn.sunline.saas.loan.product.service

import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.service.FeeFeatureService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.db.InterestFeatureModality
import cn.sunline.saas.interest.model.db.OverdueInterestFeatureModality
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.loan.product.component.LoanProductConditionComponent
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
import javax.transaction.Transactional
import javax.persistence.criteria.Predicate

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
                        newProductId,this.maxValueRange, this.minValueRange
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

        loanProduct.configurationOptions?.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration = objectMapper.convertValue<DTOAmountLoanProductConfigurationView>(it)
                    dtoLoanProduct.amountConfiguration?.run {
                        this.maxValueRange = it.getMaxValueRange()
                        this.minValueRange = it.getMinValueRange()
                    }
                }
                ConditionType.TERM ->{
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id,getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
                }
            }
        }

        dtoLoanProduct.interestFeature = interestFeature
        dtoLoanProduct.repaymentFeature = repaymentFeature
        dtoLoanProduct.feeFeatures = feeFeatures

        return dtoLoanProduct
    }


    @Transactional
    fun updateLoanProduct(id:Long,loanProductData: DTOLoanProductChange): DTOLoanProductView {
        val oldLoanProduct = this.getOne(id)?:throw Exception("Invalid loan product")
        //update loan product
        oldLoanProduct.name = loanProductData.name
        oldLoanProduct.version = (oldLoanProduct.version.toLong()+1L).toString()
        oldLoanProduct.description = loanProductData.description
        oldLoanProduct.loanPurpose = loanProductData.loanPurpose


        loanProductData.amountConfiguration?.apply {
            oldLoanProduct.configurationOptions?.forEach {
                if(this.id == it.id){
                    it.setValue(this.maxValueRange,this.minValueRange)
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

        updateLoanProduct.configurationOptions?.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration = objectMapper.convertValue<DTOAmountLoanProductConfigurationView>(it)
                    dtoLoanProduct.amountConfiguration?.run {
                        this.maxValueRange = it.getMaxValueRange()
                        this.minValueRange = it.getMinValueRange()
                    }
                }
                ConditionType.TERM ->{
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id,getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
                }
            }
        }

        //update interestFeature
        var interestFeature = interestProductFeatureService.getOneByProductId(id)
        if(interestFeature == null){
            interestFeature = loanProductData.interestFeature?.run {
                interestProductFeatureService.register(
                        id,
                        objectMapper.convertValue(loanProductData.interestFeature)
                )
            }
        } else {
            loanProductData.interestFeature?.run {
                interestFeature.ratePlanId = this.ratePlanId
                interestFeature.interest = InterestFeatureModality(interestFeature.id,this.baseYearDays,this.adjustFrequency)
                interestFeature.overdueInterest = OverdueInterestFeatureModality(interestFeature.id,this.overdueInterestRatePercentage)
            }
        }
        interestFeature?.run {
            val updateInterestFeature = interestProductFeatureService.save(interestFeature)
            dtoLoanProduct.interestFeature = updateInterestFeature
        }

        //update repaymentFeature
        var repaymentFeature = repaymentProductFeatureService.getOneByProductId(id)
        if(repaymentFeature == null){
            repaymentFeature = loanProductData.repaymentFeature?.run {
                repaymentProductFeatureService.register(
                        id,
                        objectMapper.convertValue(loanProductData.repaymentFeature)
                )
            }
        } else {
            loanProductData.repaymentFeature?.run {
                repaymentFeature.payment = RepaymentFeatureModality(repaymentFeature.id,this.paymentMethod,this.frequency,this.repaymentDayType)
                this.prepaymentFeatureModality.forEach {
                    if(it.id == null){
                        repaymentFeature.prepayment.add(
                                PrepaymentFeatureModality(
                                        seq.nextId(),
                                        it.term,
                                        it.type,
                                        it.penaltyRatio ?: BigDecimal.ZERO
                                )
                        )
                    }
                }
            }
        }

        repaymentFeature?.run {
            val updateRepaymentFeature = repaymentProductFeatureService.save(repaymentFeature)
            dtoLoanProduct.repaymentFeature = updateRepaymentFeature
        }


        //update feeFeatures
        var feeFeatures:MutableList<FeeFeature>? = feeProductFeatureService.getListByProductId(id)
        if(feeFeatures!!.isEmpty()){
            feeFeatures = loanProductData.feeFeatures?.run {
                feeProductFeatureService.register(
                        id,
                        objectMapper.convertValue(loanProductData.feeFeatures)
                )
            }
        } else {
            loanProductData.feeFeatures?.run {
                this.forEach {
                    if (it.id == null){
                        feeFeatures.add(
                                FeeFeature(
                                        seq.nextId(),
                                        id,
                                        it.feeType,
                                        it.feeMethodType,
                                        it.feeAmount,
                                        it.feeRate,
                                        it.feeDeductType
                                )
                        )
                    }
                }
            }
        }

        feeFeatures?.isNotEmpty().run {
            val updateFeeFeatures = feeProductFeatureService.save(feeFeatures!!).toMutableList()
            dtoLoanProduct.feeFeatures = updateFeeFeatures
        }
        return dtoLoanProduct
    }


    fun getLoanProduct(id:Long): DTOLoanProductView {
        val loanProduct = this.getOne(id)?:throw Exception("Invalid loan product")
        val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(loanProduct)

        loanProduct.configurationOptions?.forEach {
            when (ConditionType.valueOf(it.type)) {
                ConditionType.AMOUNT -> {
                    dtoLoanProduct.amountConfiguration = objectMapper.convertValue<DTOAmountLoanProductConfigurationView>(it)
                    dtoLoanProduct.amountConfiguration?.run {
                        this.maxValueRange = it.getMaxValueRange()
                        this.minValueRange = it.getMinValueRange()
                    }
                }
                ConditionType.TERM ->{
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id,getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
                }
            }
        }

        val interestFeature = interestProductFeatureService.getOneByProductId(id)
        val repaymentFeature = repaymentProductFeatureService.getOneByProductId(id)
        val feeFeatures = feeProductFeatureService.getListByProductId(id)
        dtoLoanProduct.interestFeature = interestFeature
        dtoLoanProduct.repaymentFeature = repaymentFeature
        dtoLoanProduct.feeFeatures = feeFeatures

        return dtoLoanProduct
    }


    private fun getValueRange(value:BigDecimal): LoanTermType? {
        for(type in LoanTermType.values()){
            if(value.toInt() == type.convertDays()){
                return type
            }
        }

        return null
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
        val product = this.getOne(id) ?: throw Exception("Invalid loan product")
        product.status = status
        return save(product)
    }
}