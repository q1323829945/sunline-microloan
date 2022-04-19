package cn.sunline.saas.disbursement.arrangement.model.dto

/**
 * @title: DTODisbursementArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 16:01
 */
data class DTODisbursementArrangementAdd(
    val paymentAccount: MutableList<DTODisbursementAccount>,
    val lendingAccount: DTODisbursementAccount
)

data class DTODisbursementAccount(
    val disbursementAccount: String,
    val disbursementAccountBank: String
)
