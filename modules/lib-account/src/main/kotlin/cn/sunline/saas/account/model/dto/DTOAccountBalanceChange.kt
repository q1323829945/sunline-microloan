package cn.sunline.saas.account.model.dto

import cn.sunline.saas.global.model.CurrencyType

/**
 * @title: DTOAccountAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 10:54
 */
data class DTOAccountBalanceChange(
    val id: Long,
    val purpose: String?,
    val currency: CurrencyType,
    val date: String,
    val amount: String
)
