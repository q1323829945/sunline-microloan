package cn.sunline.saas.service

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductDirectoryService(private val loanProductService: LoanProductService) {

    fun retrieveProduct(productId: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val view = loanProductService.getLoanProduct(productId)
        return DTOResponseSuccess(view).response()
    }
}