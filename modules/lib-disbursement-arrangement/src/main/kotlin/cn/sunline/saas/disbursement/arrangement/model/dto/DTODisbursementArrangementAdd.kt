package cn.sunline.saas.disbursement.arrangement.model.dto

import cn.sunline.saas.disbursement.arrangement.model.db.DisbursementLendType

/**
 * @title: DTODisbursementArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 16:01
 */
data class DTODisbursementArrangementAdd(
    val disbursementAccount: String,
    val disbursementAccountBank: String,
    val disbursementLendType: DisbursementLendType
)