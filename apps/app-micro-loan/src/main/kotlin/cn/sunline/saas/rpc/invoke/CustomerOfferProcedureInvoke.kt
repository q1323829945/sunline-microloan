package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.rpc.invoke.dto.DTOProductUploadConfig

interface CustomerOfferProcedureInvoke {
    fun getProductUploadConfig(productId:Long):List<DTOProductUploadConfig>

    fun getInterestRate(productId: Long):List<LoanTermType>
}