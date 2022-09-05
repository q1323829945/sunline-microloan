package cn.sunline.saas.product.controller

import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireAdd
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireChange
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireView
import cn.sunline.saas.channel.product.service.QuestionnaireService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("questionnaire")
class QuestionnaireController {
    @Autowired
    private lateinit var questionnaireService: QuestionnaireService

    @PostMapping
    fun addOne(@RequestBody dtoQuestionnaireAdd: DTOQuestionnaireAdd): ResponseEntity<DTOResponseSuccess<DTOQuestionnaireView>> {
        val questionnaire = questionnaireService.addOne(dtoQuestionnaireAdd)
        return DTOResponseSuccess(questionnaire).response()
    }

    @PutMapping
    fun updateOne(@RequestBody dtoQuestionnaireChange: DTOQuestionnaireChange): ResponseEntity<DTOResponseSuccess<DTOQuestionnaireView>> {
        val questionnaire = questionnaireService.updateOne(dtoQuestionnaireChange)
        return DTOResponseSuccess(questionnaire).response()
    }

    @GetMapping("paged")
    fun paged(pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val questionnaires = questionnaireService.paged(pageable)
        return DTOPagedResponseSuccess(questionnaires.map { it }).response()
    }

    @GetMapping
    fun getAll():ResponseEntity<DTOPagedResponseSuccess>{
        val questionnaires = questionnaireService.paged(Pageable.unpaged())
        return DTOPagedResponseSuccess(questionnaires.map { it }).response()
    }


    @GetMapping("{id}")
    fun getOne(@PathVariable id:String): ResponseEntity<DTOResponseSuccess<DTOQuestionnaireView>> {
        val questionnaire = questionnaireService.getQuestionnaire(id.toLong())
        return DTOResponseSuccess(questionnaire).response()
    }
}