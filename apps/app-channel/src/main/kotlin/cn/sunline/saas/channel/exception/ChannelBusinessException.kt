package cn.sunline.saas.channel.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class ChannelBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)