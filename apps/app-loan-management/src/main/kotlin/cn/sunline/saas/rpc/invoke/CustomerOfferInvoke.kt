package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView

interface CustomerOfferInvoke {
    fun getProduct(productId:Long): DTOLoanProductView
}