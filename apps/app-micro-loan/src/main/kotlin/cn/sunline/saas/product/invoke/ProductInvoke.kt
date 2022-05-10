package cn.sunline.saas.product.invoke

import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.response.DTOResponseSuccess


interface ProductInvoke {

    fun getProductInfoByProductId(productId: Long): DTOResponseSuccess<DTOLoanProduct>?

    fun getProductListByIdentificationCode(identificationCode:String): DTOResponseSuccess<MutableList<DTOLoanProduct>>?

}