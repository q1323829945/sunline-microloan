package cn.sunline.saas.channel.product.service

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.channel.product.model.QuestionnaireAnswerType
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireAdd
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireChange
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionnaireServiceTest {
    @Autowired
    private lateinit var questionnaireService: QuestionnaireService

    lateinit var id1:String
    lateinit var id2:String

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("0")

        val q1 = questionnaireService.addOne(
            DTOQuestionnaireAdd(
                question = "question1",
                answerType = QuestionnaireAnswerType.BOOLEAN
            )
        )
        Assertions.assertThat(q1).isNotNull
        Assertions.assertThat(q1.question).isEqualTo("question1")
        id1 = q1.id

        val q2 = questionnaireService.addOne(
            DTOQuestionnaireAdd(
                question = "question2",
                answerType = QuestionnaireAnswerType.BOOLEAN
            )
        )
        Assertions.assertThat(q2).isNotNull
        Assertions.assertThat(q2.question).isEqualTo("question2")

        id2 = q2.id
    }

    @Test
    fun `update question`(){
        val update = questionnaireService.updateOne(
            DTOQuestionnaireChange(
                id = id1,
                question = "question1alter",
                answerType = QuestionnaireAnswerType.BOOLEAN
            )
        )
        Assertions.assertThat(update.question).isEqualTo("question1alter")
    }

    @Test
    fun `get one`(){
        val question = questionnaireService.getQuestionnaire(id2.toLong())
        Assertions.assertThat(question.question).isEqualTo("question2")
    }

    @Test
    fun `get paged`(){
        val paged = questionnaireService.paged(Pageable.unpaged())
        Assertions.assertThat(paged.totalElements).isEqualTo(2)
    }
}