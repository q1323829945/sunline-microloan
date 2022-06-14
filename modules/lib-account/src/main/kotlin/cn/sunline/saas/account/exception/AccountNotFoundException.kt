package cn.sunline.saas.account.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: AccountNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/6/6 15:18
 */
class AccountNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ACCOUNT_NOT_FOUND,
) : NotFoundException(exceptionMessage, statusCode) {
}