package cn.sunline.saas.huaweicloud.obs.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

/**
 * @title: ObsUploadException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/28 15:14
 */
class ObsUploadException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.FILE_UPLOAD_FAILED,
) : SystemException(exceptionMessage, statusCode)