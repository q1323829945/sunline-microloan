package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProduct


interface ProductInvoke {

    fun getProductInfoByProductId(productId: Long): DTOInvokeLoanProduct

    fun getProductListByIdentificationCode(identificationCode:String): MutableList<DTOInvokeLoanProduct>

}