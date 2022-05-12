package cn.sunline.saas.disbursement.arrangement.exception

import cn.sunline.saas.exceptions.BadRequestException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: DisbursementLendTypeNotSupported
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 14:37
 */
class DisbursementLendTypeNotSupported(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DISBURSEMENT_LEND_TYPE_NOT_SUPPORTED
) : BadRequestException(
    exceptionMessage,
    statusCode
)