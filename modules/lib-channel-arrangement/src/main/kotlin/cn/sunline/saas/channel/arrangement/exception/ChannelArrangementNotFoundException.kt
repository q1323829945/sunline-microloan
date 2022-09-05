package cn.sunline.saas.channel.arrangement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ChannelArrangementNotFoundException(
    exceptionMessage: String?=null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CHANNEL_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)