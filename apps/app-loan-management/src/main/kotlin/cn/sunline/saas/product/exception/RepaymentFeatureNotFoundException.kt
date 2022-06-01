package cn.sunline.saas.product.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class RepaymentFeatureNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.REPAYMENT_FEATURE_NOT_FOUND,
) : NotFoundException(exceptionMessage, statusCode)