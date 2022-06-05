package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.underwriting.controllers.dto.DTOUnderwriting
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.service.UnderwritingManagementService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("UnderwritingManagement")
class UnderwritingManagementController {

    @Autowired
    private lateinit var underwritingManagementService: UnderwritingManagementService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(@PathParam("name") name:String?,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = underwritingManagementService.getPaged(name, pageable)
        return DTOPagedResponseSuccess(page.map { getDTOUnderwriting(it) }).response()
    }


    @PutMapping("{id}")
    fun update(@PathVariable("id")id:String,@RequestBody dtoUnderwritingChange: DTOUnderwriting):ResponseEntity<DTOResponseSuccess<DTOUnderwriting>>{
        val oldOne = underwritingManagementService.getOne(id.toLong())?: throw UnderwritingNotFound("Invalid underwriting")
        val newOne = objectMapper.convertValue<Underwriting>(dtoUnderwritingChange)

        val updateOne = underwritingManagementService.update(oldOne,newOne)
        return DTOResponseSuccess(getDTOUnderwriting(updateOne)).response()
    }

    @GetMapping("{id}")
    fun getDetail(@PathVariable("id")id:String):ResponseEntity<DTOResponseSuccess<DTOUnderwriting>>{
        val underwriting = underwritingManagementService.getOne(id.toLong())?: throw UnderwritingNotFound("Invalid underwriting")
        return DTOResponseSuccess(getDTOUnderwriting(underwriting)).response()
    }

    @PutMapping("approval/{id}")
    fun approval(@PathVariable(name = "id")id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        underwritingManagementService.approval(id.toLong())

        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("rejected/{id}")
    fun rejected(@PathVariable(name = "status")underwritingType: UnderwritingType,
                     @PathVariable(name = "id")id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        underwritingManagementService.rejected(id.toLong())

        return DTOResponseSuccess(Unit).response()
    }

    private fun getDTOUnderwriting(underwriting: Underwriting):DTOUnderwriting{
        return DTOUnderwriting(
            underwriting.id.toString(),
            underwriting.customerId.toString(),
            objectMapper.convertValue(underwriting.applicationData),
            underwriting.customerCreditRate,
            underwriting.creditRisk,
            underwriting.fraudEvaluation,
            underwriting.regulatoryCompliance,
            underwriting.status
        )
    }

}