package cn.sunline.saas.repayment.schedule.invoke

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.http.ResponseEntity

interface LoanProductDirectoryService {

    fun getProductDirectory(productId: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>>
}