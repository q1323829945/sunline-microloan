package cn.sunline.saas.channel.party.organisation.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class BusinessUnitNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ORGANISATION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)