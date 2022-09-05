package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.Purpose
import java.math.BigDecimal


data class DTOKabuhayanLoan(
    val applicationId:String,
    val personalInformation: DTOPersonalInformation? = null,
    val companyInformation: DTOCompanyInformation? = null,
    val loanInformation: DTOLoanInformation? = null,
    val questionnaires: List<DTOQuestionnaire>? = null,
    var signature:String? = null,
    val channel:DTOChannelInformation,
)

data class DTOPersonalInformation(
    val name: DTONameInformation? = null,
    val mobile:String? = null,
    val email:String? = null,
)

data class DTOCompanyInformation(
    val name:String? = null,
    val nature:String? = null,
    val services:String? = null,
    val operatingYears:Int? = null,
    val address: DTOAddressInformation? = null,
)

data class DTOLoanInformation(
    val amount:BigDecimal? = null,
    val term:LoanTermType? = null,
    val purpose:List<String>? = null,
)

data class DTOQuestionnaire(
    val question:String? = null,
    val answer:Boolean? = null,
)

