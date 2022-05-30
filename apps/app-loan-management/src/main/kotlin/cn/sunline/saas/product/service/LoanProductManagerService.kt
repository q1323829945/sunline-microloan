package cn.sunline.saas.product.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.global.model.TermType
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.exception.LoanProductBusinessException
import cn.sunline.saas.product.exception.LoanProductNotFoundException
import cn.sunline.saas.product.service.dto.DTOLoanProductResponse
import cn.sunline.saas.product.service.dto.DTOLoanUploadConfigure
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.ProductInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@Service
class LoanProductManagerService(
    val productInvoke: ProductInvoke
) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    fun getPaged(
        name: String?,
        loanProductType: LoanProductType?,
        loanPurpose: String?,
        pageable: Pageable
    ): Page<DTOLoanProductView> {
        val paged = loanProductService.getLoanProductPaged(name, loanProductType, loanPurpose, pageable)
        return paged.map { objectMapper.convertValue<DTOLoanProductView>(it) }
    }

    fun addOne(loanProductData: DTOLoanProduct): DTOLoanProductView {
        checkTermConditions(
            loanProductData.termConfiguration.maxValueRange.term,
            loanProductData.termConfiguration.minValueRange.term
        )
        checkAmountConditions(
            BigDecimal(loanProductData.amountConfiguration.maxValueRange),
            BigDecimal(loanProductData.amountConfiguration.maxValueRange)
        )
        val oldLoanProduct =
            loanProductService.findByIdentificationCode(loanProductData.identificationCode).maxByOrNull {
                it.version
            }

        if (oldLoanProduct != null && oldLoanProduct.status == BankingProductStatus.INITIATED) {
            throw LoanProductBusinessException(
                "The version of loan's product has already exist",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }

        loanProductData.repaymentFeature?.prepaymentFeatureModality?.groupBy { it.term }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.feeFeatures?.groupBy { it.feeType }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        loanProductData.version = oldLoanProduct?.run {
            updateStatus(this.id.toLong(),BankingProductStatus.OBSOLETE)
            (this.version.toInt() + 1).toString()
        }?: "1"

        return addBusinessUnitType(loanProductService.register(loanProductData))
    }

    fun getOne(id: Long): DTOLoanProductView {
        return addBusinessUnitType(loanProductService.getLoanProduct(id))
        //return loanProductService.getLoanProduct(id)

    }

    fun updateOne(
        id: Long,
        dtoLoanProduct: DTOLoanProduct
    ): DTOLoanProductView {
        checkProductStatus(id, false)
        checkTermConditions(
            dtoLoanProduct.termConfiguration.maxValueRange.term,
            dtoLoanProduct.termConfiguration.minValueRange.term
        )
        checkAmountConditions(
            BigDecimal(dtoLoanProduct.amountConfiguration.maxValueRange),
            BigDecimal(dtoLoanProduct.amountConfiguration.maxValueRange)
        )

        dtoLoanProduct.repaymentFeature?.prepaymentFeatureModality?.groupBy { it.term }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the type of prepayment's term was already exist,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        dtoLoanProduct.feeFeatures?.groupBy { it.feeType }?.map { it ->
            val count = it.value.count()
            if (count > 1) {
                throw LoanProductBusinessException(
                    "the feeType of feeFeatures was more than once,only one allowed",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        return addBusinessUnitType(loanProductService.updateLoanProduct(id, dtoLoanProduct))
    }

    fun updateStatus(
        id: Long,
        dtoLoanProductStatus: BankingProductStatus
    ): ResponseEntity<DTOResponseSuccess<LoanProduct>> {
        checkProductStatus(id, true)
        val view = loanProductService.updateLoanProductStatus(id, dtoLoanProductStatus)
        return DTOResponseSuccess(view).response()
    }

    fun getProductInfo(identificationCode: String): List<DTOLoanProductView> {
        return loanProductService.findByIdentificationCode(identificationCode)
    }

    fun getLoanProductHistoryList(identificationCode: String): List<DTOLoanProductView> {
        return loanProductService.findByIdentificationCodeAndStatus(identificationCode, BankingProductStatus.OBSOLETE)
    }

    fun getLoanProductListByStatus(
        bankingProductStatus: BankingProductStatus?,
        pageable: Pageable
    ): Page<LoanProduct> {
        val status = bankingProductStatus?.name ?: BankingProductStatus.SOLD.name
        return loanProductService.getLoanProductListByStatus(status, pageable)
    }

    private fun checkTermConditions(termMaxValueRange: TermType, termMinValueRange: TermType) {
        if (termMaxValueRange < termMinValueRange) {
            throw LoanProductBusinessException(
                "The term's config of product was error",
                ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR
            )
        }
    }

    private fun checkAmountConditions(amountMaxValueRange: BigDecimal, amountMinValueRange: BigDecimal) {
        if (amountMaxValueRange < amountMinValueRange) {
            throw LoanProductBusinessException(
                "The amount's config of product was error",
                ManagementExceptionCode.PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR
            )
        }
    }

    private fun checkProductStatus(id: Long, isUpdateStatus: Boolean) {
        val loanProduct = loanProductService.getLoanProduct(id)
        val status = loanProduct.status
        if (isUpdateStatus && BankingProductStatus.OBSOLETE == status) {
            throw LoanProductBusinessException(
                "The status of product was OBSOLETE,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
        if (isUpdateStatus && BankingProductStatus.SOLD == status) {
            throw LoanProductBusinessException(
                "The status of product was SOLD,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
        if (!isUpdateStatus && BankingProductStatus.INITIATED != status) {
            throw LoanProductBusinessException(
                "The status of product was not INITIATED,non-supported update",
                ManagementExceptionCode.PRODUCT_STATUS_ERROR
            )
        }
    }


    fun getInvokeOne(productId:Long):DTOLoanProductResponse{
        val product = loanProductService.getLoanProduct(productId)
        val list = productInvoke.getInterestRate(product.interestFeature!!.ratePlanId.toLong())
        val loanProduct = objectMapper.convertValue<DTOLoanProductResponse>(product)
        loanProduct.interestFeature.ratePlan = objectMapper.convertValue(list)
        return loanProduct
    }


    fun getUploadConfig(id:Long):List<DTOLoanUploadConfigure>{
        val product = loanProductService.getOne(id)?: throw LoanProductNotFoundException("Invalid product")
        return  product.loanUploadConfigureFeatures?.map {
            objectMapper.convertValue(it)
        }?: listOf()
    }

    private fun addBusinessUnitType(product:DTOLoanProductView):DTOLoanProductView{
        val businessUnit = productInvoke.getBusinessUnit(product.businessUnit.toLong())
        product.business = objectMapper.convertValue(businessUnit)
        return product
    }
}