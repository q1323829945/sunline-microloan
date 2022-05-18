package cn.sunline.saas.library.controller

import cn.sunline.saas.document.model.*
import cn.sunline.saas.document.service.DocumentService
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
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
        val documentId: String,
        val documentName: String,
        val documentVersion: String,
        val documentType: DocumentType,
        val directoryId: String,
        val documentFormat: DocumentFormat,
        val creationDate: Date,
        val documentLocation: String,
        val involvement: DTODocumentInvolvement,
        var documentStoreReference:String?
    )

    data class DTODocumentInvolvement(val partyId: String, val involvementType: DocumentInvolvementType)

    data class DTOResponseDocumentDirectoryEntry(
        val documentId: String,
        val documentName: String,
        val documentVersion: String,
        val documentType: DocumentType,
        val directoryId: String,
        val documentFormat: DocumentFormat,
        val creationDate: Date,
        val documentLocation: String,
        val involvement: DTODocumentInvolvement?,
        val documentStatus: DocumentStatus
    )

    @Autowired
    private lateinit var documentService:DocumentService

    @Autowired
    private lateinit var huaweiService: ObsApi


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping("Register")
    fun register(@RequestBody dtoDocumentDirectoryEntry: DTODocumentDirectoryEntry): ResponseEntity<DTOResponseSuccess<DTOResponseDocumentDirectoryEntry>> {

        val key = dtoDocumentDirectoryEntry.creationDate.time.toString() + dtoDocumentDirectoryEntry.documentName

        huaweiService.putObject(PutParams(key,dtoDocumentDirectoryEntry.documentLocation))

        dtoDocumentDirectoryEntry.documentStoreReference = key
        val document = objectMapper.convertValue<Document>(dtoDocumentDirectoryEntry)

        val saveDocument = documentService.save(document)

        val responseDocument = objectMapper.convertValue<DTOResponseDocumentDirectoryEntry>(saveDocument)

        return DTOResponseSuccess(responseDocument).response()
    }

}