package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.YesOrNo

data class DTORepaymentAccountAdd(
    val agreementId: Long,
    val repaymentAccount: String,
    val repaymentAccountBank: String,
    val mobileNumber: String
)

data class DTORepaymentAccountView(
    val agreementId: String,
    val repaymentAccountLines: MutableList<DTORepaymentAccountLineView>
)

data class DTORepaymentAccountLineView(
    val id: String,
    val repaymentAccount: String,
    val repaymentAccountBank: String,
    var status : YesOrNo
)

