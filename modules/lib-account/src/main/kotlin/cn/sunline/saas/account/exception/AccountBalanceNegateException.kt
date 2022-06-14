package cn.sunline.saas.account.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: AccountBalanceNegateException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/6/7 9:00
 */
class AccountBalanceNegateException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ACCOUNT_BALANCE_NEGATE,
) : BusinessException(exceptionMessage, statusCode) {
}