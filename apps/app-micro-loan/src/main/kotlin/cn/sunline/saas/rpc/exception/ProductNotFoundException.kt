package cn.sunline.saas.rpc.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ProductNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PRODUCT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)