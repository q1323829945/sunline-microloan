package cn.sunline.saas.channel.party.person.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class PersonNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PERSON_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)