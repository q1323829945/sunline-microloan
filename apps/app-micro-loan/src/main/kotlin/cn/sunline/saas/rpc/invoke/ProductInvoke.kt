package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProduct
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProducts


interface ProductInvoke {

    fun getProductInfoByProductId(productId: Long): DTOInvokeLoanProduct

    fun getProductListByIdentificationCode(identificationCode:String): MutableList<DTOInvokeLoanProduct>

    fun getProductsByStatus(status:String):MutableList<DTOInvokeLoanProducts>
}