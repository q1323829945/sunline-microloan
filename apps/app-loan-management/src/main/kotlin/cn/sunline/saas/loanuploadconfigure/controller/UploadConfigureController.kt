package cn.sunline.saas.loanuploadconfigure.controller

import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureAdd
import cn.sunline.saas.loan.configure.modules.dto.DTOUploadConfigureView
import cn.sunline.saas.loan.configure.services.LoanUploadConfigureService
import cn.sunline.saas.loanuploadconfigure.exception.ConfigureNotFoundException
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.criteria.Predicate

@RestController
@RequestMapping("/LoanUploadConfigure")
class UploadConfigureController {
    @Autowired
    private lateinit var uploadConfigureService: LoanUploadConfigureService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{

        val paged = uploadConfigureService.getPaged({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"),false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable)

        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOUploadConfigureView>(it) }).response()
    }

    @PostMapping
    fun addUploadConfigure(@RequestBody dtoUploadConfigureAdd: DTOUploadConfigureAdd): ResponseEntity<DTOResponseSuccess<DTOUploadConfigureView>>{
        val uploadConfigure = objectMapper.convertValue<LoanUploadConfigure>(dtoUploadConfigureAdd)
        val save = uploadConfigureService.addOne(uploadConfigure)
        val responseEntity = objectMapper.convertValue<DTOUploadConfigureView>(save)

        return DTOResponseSuccess(responseEntity).response()
    }

    @DeleteMapping("{id}")
    fun deleteUploadConfigure(@PathVariable id:Long): ResponseEntity<DTOResponseSuccess<DTOUploadConfigureView>>{
        val uploadConfigure = uploadConfigureService.getOne(id)?:throw ConfigureNotFoundException("Invalid configure")
        uploadConfigure.deleted = true
        val save = uploadConfigureService.save(uploadConfigure)
        val responseEntity = objectMapper.convertValue<DTOUploadConfigureView>(save)

        return DTOResponseSuccess(responseEntity).response()
    }
}