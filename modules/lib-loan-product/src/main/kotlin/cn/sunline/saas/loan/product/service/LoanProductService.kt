package cn.sunline.saas.loan.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.service.FeeFeatureService
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.db.InterestFeatureModality
import cn.sunline.saas.interest.model.db.OverdueInterestFeatureModality
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
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
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
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

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Transactional
    fun register(loanProductData: DTOLoanProductAdd): DTOLoanProductView {
        val newProductId = seq.nextId()
        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach{
            val loanUploadConfigure = loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }
        val loanProductAdd = LoanProduct(
            newProductId,
            loanProductData.identificationCode,
            loanProductData.name,
            loanProductData.version!!,
            loanProductData.description,
            loanProductData.loanProductType,
            loanProductData.loanPurpose,
            loanUploadConfigureList
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
        val nowProduct = this.getOne(id) ?: throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        if(status == BankingProductStatus.SOLD){
           val productList = loanProductRepos.findByIdentificationCode(nowProduct.identificationCode)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
            for(product in productList){
                if(BankingProductStatus.SOLD == product.status){
                    product.status = BankingProductStatus.OBSOLETE
                    save(product)
                }
            }
        }
        nowProduct.status = status
        return save(nowProduct)
    }


    fun findByIdentificationCode(identificationCode:String):MutableList<DTOLoanProductView>{
        val productList = loanProductRepos.findByIdentificationCode(identificationCode)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        var list = ArrayList<DTOLoanProductView>()
        for(product in productList){
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(product)
            setConfigurationOptions(product,dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
    }

    fun findByIdentificationCodeAndStatus(identificationCode:String,bankingProductStatus: BankingProductStatus):MutableList<DTOLoanProductView>{
        val productList = loanProductRepos.findByIdentificationCodeAndStatus(identificationCode,bankingProductStatus)?:throw LoanProductNotFoundException("Invalid loan product",ManagementExceptionCode.PRODUCT_NOT_FOUND)
        var list = ArrayList<DTOLoanProductView>()
        for(product in productList){
            val dtoLoanProduct = objectMapper.convertValue<DTOLoanProductView>(product)
            setConfigurationOptions(product,dtoLoanProduct)
            list.add(dtoLoanProduct)
        }
        return list
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
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id.toString(),getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
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

    fun getLoanProductListByStatus(bankingProductStatus:String,pageable: Pageable): Page<LoanProduct> {
        return loanProductRepos.getLoanProductListByStatus(bankingProductStatus,pageable)
    }


    @Transactional
    fun updateLoanProduct(id:Long,loanProductData: DTOLoanProductChange): DTOLoanProductView {
        val oldLoanProduct = this.getOne(id)?:throw Exception("Invalid loan product")

        val loanUploadConfigureList = ArrayList<LoanUploadConfigure>()
        loanProductData.loanUploadConfigureFeatures?.forEach{
            val loanUploadConfigure = loanUploadConfigureService.getOne(it) ?: throw Exception("loanUploadConfigure Not Found")
            loanUploadConfigureList.add(loanUploadConfigure)
        }
        oldLoanProduct.loanUploadConfigureFeatures = loanUploadConfigureList

        //update loan product
        loanProductData.amountConfiguration?.apply {
            oldLoanProduct.configurationOptions?.forEach {
                if(this.id == it.id){
                    it.setValue(BigDecimal(this.maxValueRange),BigDecimal(this.minValueRange))
                }
            }
        }

        loanProductData.termConfiguration?.apply {
            oldLoanProduct.configurationOptions?.forEach {
                if(this.id == it.id){
                    it.setValue(
                        this.maxValueRange?.days?.let { it1 -> BigDecimal(it1) },
                        this.minValueRange?.days?.let { it1 -> BigDecimal(it1) }
                    )
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
                        this.maxValueRange = it.getMaxValueRange().toString()
                        this.minValueRange = it.getMinValueRange().toString()
                    }
                }
                ConditionType.TERM ->{
                    dtoLoanProduct.termConfiguration = DTOTermLoanProductConfigurationView(it.id.toString(),getValueRange(it.getMaxValueRange()),getValueRange(it.getMinValueRange()))
                }
            }
        }

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
                interestFeature.interestType = this.interestType
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

    fun getLoanProductLoanUploadConfigureMapping(loanUploadConfigureId: Long): Long{
        return loanProductRepos.getLoanProductLoanUploadConfigureMapping(loanUploadConfigureId)
    }
}