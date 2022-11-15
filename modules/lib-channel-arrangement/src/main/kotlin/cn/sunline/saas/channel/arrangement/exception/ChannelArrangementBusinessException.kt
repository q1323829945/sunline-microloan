package cn.sunline.saas.channel.arrangement.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode


class ChannelArrangementBusinessException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CHANNEL_ARRANGEMENT_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)