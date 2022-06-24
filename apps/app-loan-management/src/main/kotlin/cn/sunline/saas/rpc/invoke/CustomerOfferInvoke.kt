package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.rpc.invoke.dto.DTOLoanAgreementView
import cn.sunline.saas.rpc.invoke.dto.DTOLoanAgreementViewInfo
import cn.sunline.saas.rpc.invoke.dto.DTOUnderwriting

interface CustomerOfferInvoke {
    fun getProduct(productId:Long): DTOLoanProductView

    fun getUnderwriting(applicationId:Long): DTOUnderwriting?

    fun getLoanAgreement(applicationId:Long): DTOLoanAgreementView?

    fun getLoanAgreementInfo(applicationId:Long): DTOLoanAgreementViewInfo?

    fun getLoanAgreementInfoByAgreementId(agreementId:Long): DTOLoanAgreementViewInfo?
}