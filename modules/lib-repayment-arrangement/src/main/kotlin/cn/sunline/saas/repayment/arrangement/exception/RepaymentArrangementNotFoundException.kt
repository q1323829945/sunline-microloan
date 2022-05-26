package cn.sunline.saas.repayment.arrangement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


/**
 * @title: RepaymentArrangementNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 16:37
 */
class RepaymentArrangementNotFoundException (
    exceptionMessage: String?=null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.REPAYMENT_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)