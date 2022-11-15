package cn.sunline.saas.channel.agreement.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class ChannelAgreementBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CHANNEL_COMMISSION_AGREEMENT_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)