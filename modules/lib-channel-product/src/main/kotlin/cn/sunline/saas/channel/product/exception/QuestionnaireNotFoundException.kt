package cn.sunline.saas.channel.product.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: LoanProductNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/28 15:51
 */
class QuestionnaireNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.QUESTIONNAIRE_NOT_FOUND,
) : NotFoundException(exceptionMessage, statusCode)