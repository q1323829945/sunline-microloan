package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.rpc.invoke.dto.DTOUnderwriting

interface CustomerOfferInvoke {
    fun getProduct(productId:Long): DTOLoanProductView

    fun getUnderwriting(applicationId:Long): DTOUnderwriting?
}