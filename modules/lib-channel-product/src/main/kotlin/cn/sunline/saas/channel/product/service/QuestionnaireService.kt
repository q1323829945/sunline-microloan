package cn.sunline.saas.channel.product.service

import cn.sunline.saas.channel.product.exception.QuestionnaireNotFoundException
import cn.sunline.saas.channel.product.model.db.Questionnaire
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireAdd
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireChange
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireView
import cn.sunline.saas.channel.product.repository.QuestionnaireRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class QuestionnaireService (
    private var questionnaireRepos: QuestionnaireRepository,
    private var sequence: Sequence) : BaseMultiTenantRepoService<Questionnaire, Long>(questionnaireRepos) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoQuestionnaireAdd: DTOQuestionnaireAdd):DTOQuestionnaireView{
        val questionnaire = save(
            Questionnaire(
                id = sequence.nextId(),
                question = dtoQuestionnaireAdd.question,
                answerType = dtoQuestionnaireAdd.answerType
            )
        )

        return objectMapper.convertValue(questionnaire)
    }

    fun updateOne(dtoQuestionnaireChange: DTOQuestionnaireChange):DTOQuestionnaireView{
        val oldOne = getOne(dtoQuestionnaireChange.id.toLong())?: throw QuestionnaireNotFoundException("Invalid questionnaire")
        oldOne.question = dtoQuestionnaireChange.question
        oldOne.answerType = dtoQuestionnaireChange.answerType

        val questionnaire = save(oldOne)

        return objectMapper.convertValue(questionnaire)
    }

    fun getQuestionnaire(id:Long):DTOQuestionnaireView{
        val questionnaire = getOne(id)?: throw QuestionnaireNotFoundException("Invalid questionnaire")
        return objectMapper.convertValue(questionnaire)
    }

    fun paged(pageable: Pageable): Page<DTOQuestionnaireView> {
        val paged = getPageWithTenant(null,pageable)
        return paged.map { objectMapper.convertValue(it) }
    }
}