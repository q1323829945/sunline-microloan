package cn.sunline.saas.product.service

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductChange
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.product.dto.DTOLoanProductStatus
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


interface LoanProductManagerService {
    fun getPaged(name:String?,
                 loanProductType: LoanProductType?,
                 loanPurpose: String?,
                 pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess>

    fun addOne(loanProductData: DTOLoanProductAdd): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>>

    fun getOne(id: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>>

    fun updateOne(id: Long, dtoLoanProduct: DTOLoanProductChange): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>>

    fun updateStatus(id: Long, dtoLoanProductStatus: DTOLoanProductStatus): ResponseEntity<DTOResponseSuccess<LoanProduct>>

    fun getProductInfo(identificationCode:String): ResponseEntity<DTOPagedResponseSuccess>

    fun getLoanProductHistoryList(identificationCode:String): ResponseEntity<DTOPagedResponseSuccess>

    fun getLoanProductListByStatus(bankingProductStatus: BankingProductStatus?,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>
}