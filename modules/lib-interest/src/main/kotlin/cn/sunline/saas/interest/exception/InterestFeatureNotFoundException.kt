package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class InterestFeatureNotFoundException  (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INTEREST_FEATURE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)