package cn.sunline.saas.document.library.controller

import cn.sunline.saas.document.model.DocumentFormat
import cn.sunline.saas.document.model.DocumentInvolvementType
import cn.sunline.saas.document.model.DocumentStatus
import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @title: DocumentLibraryController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/22 11:13
 */
@RestController
@RequestMapping("DocumentLibrary")
class DocumentLibraryController {

    data class DTODocumentDirectoryEntry(
        val documentName: String,
        val documentVersion: String,
        val documentType: DocumentType,
        val directoryId: Long,
        val documentFormat: DocumentFormat,
        val creationDate: Date,
        val documentLocation: String,
        val involvement: DTODocumentInvolvement,
    )

    data class DTODocumentInvolvement(val partyId: Long, val involvementType: DocumentInvolvementType)

    data class DTOResponseDocumentDirectoryEntry(
        val documentId: Long,
        val documentName: String,
        val documentVersion: String,
        val documentType: DocumentType,
        val directoryId: Long,
        val documentFormat: DocumentFormat,
        val creationDate: Date,
        val documentLocation: String,
        val involvement: DTODocumentInvolvement,
        val documentStatus: DocumentStatus
    )

    @PostMapping("Register")
    fun register(@RequestBody dtoDocumentDirectoryEntry: DTODocumentDirectoryEntry): ResponseEntity<DTOResponseSuccess<DTOResponseDocumentDirectoryEntry>> {

        return DTOResponseSuccess(DTOResponseDocumentDirectoryEntry()).response()
    }
}