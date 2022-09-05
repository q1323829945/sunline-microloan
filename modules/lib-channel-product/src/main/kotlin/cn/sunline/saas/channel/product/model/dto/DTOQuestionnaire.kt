package cn.sunline.saas.channel.product.model.dto

import cn.sunline.saas.channel.product.model.QuestionnaireAnswerType

data class DTOQuestionnaireAdd(
    val question:String,
    val answerType: QuestionnaireAnswerType
)

data class DTOQuestionnaireChange(
    val id:String,
    val question:String,
    val answerType: QuestionnaireAnswerType
)

data class DTOQuestionnaireView(
    val id:String,
    val question:String,
    val answerType: QuestionnaireAnswerType
)