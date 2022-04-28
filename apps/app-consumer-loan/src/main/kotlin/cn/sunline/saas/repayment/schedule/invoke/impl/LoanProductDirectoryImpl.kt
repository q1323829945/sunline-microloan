package cn.sunline.saas.repayment.schedule.invoke.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.repayment.schedule.invoke.LoanProductDirectoryService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import io.dapr.client.domain.HttpExtension
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class LoanProductDirectoryImpl: LoanProductDirectoryService {

    private val applId = "app-product-directory"

    override fun getProductDirectory(productId: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        val result =  DaprHelper.invoke(
            applId,
            "/Production/{productId}/Retrieve",
            "{\"name\":\"$productId\"}",
            HttpExtension.GET,
            DTOLoanProductView::class.java
        )
        return DTOResponseSuccess(result).response()
    }
}