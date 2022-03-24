package cn.sunline.saas.fee.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: FeeException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/15 9:53
 */
class FeeConfigException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.FEE_CONFIG_ERROR,
) : BusinessException(exceptionMessage, statusCode)