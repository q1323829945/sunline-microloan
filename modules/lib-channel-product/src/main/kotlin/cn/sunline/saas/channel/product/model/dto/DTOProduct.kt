package cn.sunline.saas.channel.product.model.dto

import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireView
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.ProductType

data class  DTOProductAdd (
    val name:String,
    val productType: ProductType,
    val description:String? = null,
    val ratePlanId:String,
    val questionnaires: MutableList<String>? = null
)

data class DTOProductChange(
    val id:String,
    val name:String,
    val productType: ProductType,
    val description:String? = null,
    val ratePlanId:String,
    val questionnaires: MutableList<String>? = null
)

data class DTOProductView(
    val id:String,
    val name:String,
    val productType: ProductType,
    val description:String? = null,
    val ratePlanId:String,
    val questionnaires: MutableList<DTOQuestionnaireView>? = null
)

data class DTOProductAppView(
    val productId:String,
    val productType:ProductType,
    val term:List<LoanTermType>,
    val questionnaires: MutableList<DTOQuestionnaireView>? = null
)

data class DTOProductSimpleView(
    val id:String,
    val productType:ProductType,
    val name:String
)