package cn.sunline.saas.consumer.loan.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: DisbursementArrangementNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 14:25
 */
class DisbursementArrangementNotFoundException (
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DISBURSEMENT_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)