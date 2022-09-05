package cn.sunline.saas.channel.agreement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class ChannelAgreementNotFoundException(
    exceptionMessage: String?=null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CHANNEL_AGREEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)