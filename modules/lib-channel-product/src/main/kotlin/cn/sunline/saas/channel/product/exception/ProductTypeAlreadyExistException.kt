package cn.sunline.saas.channel.product.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ProductTypeAlreadyExistException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PRODUCT_TYPE_ALREADY_EXIST,
) : BusinessException(exceptionMessage, statusCode)