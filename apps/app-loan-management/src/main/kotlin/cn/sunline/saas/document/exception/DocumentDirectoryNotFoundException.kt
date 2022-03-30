package cn.sunline.saas.document.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: DocumentDirectoryNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:21
 */
class DocumentDirectoryNotFoundException  (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DOCUMENT_DIRECTORY_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)