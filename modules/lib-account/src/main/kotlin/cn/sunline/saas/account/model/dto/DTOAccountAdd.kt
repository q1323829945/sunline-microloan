package cn.sunline.saas.account.model.dto

import cn.sunline.saas.global.model.CurrencyType
import java.util.Date

/**
 * @title: DTOAccountAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 10:54
 */
data class DTOAccountAdd(
    val id: Long,
    val purpose: String?,
    val currency: CurrencyType,
    val amount: String,
    val date: String,
    val businessUnit: Long,
    val customerId: Long
)
