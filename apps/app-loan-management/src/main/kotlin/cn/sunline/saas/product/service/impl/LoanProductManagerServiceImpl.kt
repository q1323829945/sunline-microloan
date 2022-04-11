package cn.sunline.saas.product.service.impl

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductChange
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.product.dto.DTOLoanProductStatus
import cn.sunline.saas.product.service.LoanProductManagerService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@Service
class LoanProductManagerServiceImpl: LoanProductManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var loanProductService: LoanProductService

    override fun getPaged(name:String?,
                 loanProductType: LoanProductType?,
                 loanPurpose: String?,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = loanProductService.getLoanProductPaged(name,loanProductType,loanPurpose,pageable)
        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOLoanProductView>(it)}).response()
    }

    override fun addOne(loanProductData: DTOLoanProductAdd): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        loanProductData.version = "1"
        val termMaxValueRange = loanProductData.termConfiguration?.maxValueRange?.days
        val termMinValueRange = loanProductData.termConfiguration?.minValueRange?.days
        if(termMaxValueRange!! < termMinValueRange!!){
            throw ManagementException(ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR)
        }
        val amountMaxValueRange = BigDecimal(loanProductData.amountConfiguration?.maxValueRange!!)
        val amountMinValueRange = BigDecimal(loanProductData.amountConfiguration?.maxValueRange!!)
        if(amountMaxValueRange < amountMinValueRange){
            throw ManagementException(ManagementExceptionCode.PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR)
        }
        val oldLoanProduct = loanProductService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("identificationCode"), loanProductData.identificationCode))
            predicates.add(criteriaBuilder.equal(root.get<Long>("version"), loanProductData.version))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()
        if(oldLoanProduct != null){
            throw ManagementException(ManagementExceptionCode.PRODUCT_EXIST)
        }
        val view = loanProductService.register(loanProductData)
        return DTOResponseSuccess(view).response()
    }

    override fun getOne(id: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.getLoanProduct(id)
        return DTOResponseSuccess(view).response()
    }

    override fun updateOne(id: Long, dtoLoanProduct: DTOLoanProductChange): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val termMaxValueRange = dtoLoanProduct.termConfiguration?.maxValueRange?.days
        val termMinValueRange = dtoLoanProduct.termConfiguration?.minValueRange?.days
        if(termMaxValueRange!! < termMinValueRange!!){
            throw ManagementException(ManagementExceptionCode.PRODUCT_TERM_CONFIG_MAX_MIN_ERROR)
        }
        val amountMaxValueRange = BigDecimal(dtoLoanProduct.amountConfiguration?.maxValueRange!!)
        val amountMinValueRange = BigDecimal(dtoLoanProduct.amountConfiguration?.maxValueRange!!)
        if(amountMaxValueRange < amountMinValueRange){
            throw ManagementException(ManagementExceptionCode.PRODUCT_AMOUNT_CONFIG_MAX_MIN_ERROR)
        }
        val view = loanProductService.updateLoanProduct(id,dtoLoanProduct)
        return DTOResponseSuccess(view).response()

    }

    override fun updateStatus(id: Long, dtoLoanProduct: DTOLoanProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>> {

        val loanProduct = loanProductService.updateLoanProductStatus(id,dtoLoanProduct.status)

        return DTOResponseSuccess(loanProduct).response()

    }

    override fun getProductInfo(identificationCode:String): ResponseEntity<DTOPagedResponseSuccess> {
        val productList = loanProductService.findByIdentificationCode(identificationCode)
        return DTOPagedResponseSuccess(productList.map { objectMapper.convertValue<DTOLoanProductView>(it)}).response()
    }

}