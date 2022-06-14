package cn.sunline.saas.account.component

import cn.sunline.saas.account.exception.AccountBalanceNegateException
import cn.sunline.saas.account.model.db.AccountBalance
import org.joda.time.DateTime
import java.math.BigDecimal

/**
 * @title: AccountBalanceComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/6/7 9:21
 */

fun AccountBalance.increase(amount: BigDecimal, dt: DateTime) {
    this.accountBalance = this.accountBalance.add(amount)
    this.accountBalanceDate = dt.toDate()
}

fun AccountBalance.reduce(amount: BigDecimal, dt: DateTime) {
    if (amount > this.accountBalance)
        throw AccountBalanceNegateException("error: account balance is negated")
    this.accountBalance = this.accountBalance.subtract(amount)
    this.accountBalanceDate = dt.toDate()
}