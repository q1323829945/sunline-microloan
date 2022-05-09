package cn.sunline.saas.customer_offer.exceptions

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class CustomerOfferStatusException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CUSTOMER_OFFER_STATUS_ERROR
): ManagementException(statusCode,exceptionMessage)