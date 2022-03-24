package cn.sunline.saas.huaweicloud.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

/**
 * @title: ObsUploadException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 8:03
 */
class ObsBodyTypeException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.BODY_TYPE_ERROR,
): SystemException(exceptionMessage,statusCode)