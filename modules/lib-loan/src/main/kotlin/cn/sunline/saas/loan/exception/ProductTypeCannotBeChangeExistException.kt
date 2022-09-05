package cn.sunline.saas.loan.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ProductTypeCannotBeChangeExistException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PRODUCT_TYPE_CANNOT_BE_CHANGE,
) : BusinessException(exceptionMessage, statusCode)