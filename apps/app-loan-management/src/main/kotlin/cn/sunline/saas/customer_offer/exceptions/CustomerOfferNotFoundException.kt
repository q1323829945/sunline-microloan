package cn.sunline.saas.customer_offer.exceptions

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class CustomerOfferNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CUSTOMER_OFFER_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)