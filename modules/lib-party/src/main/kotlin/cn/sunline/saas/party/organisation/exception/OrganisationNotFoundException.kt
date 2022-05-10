package cn.sunline.saas.party.organisation.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class OrganisationNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ORGANISATION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)