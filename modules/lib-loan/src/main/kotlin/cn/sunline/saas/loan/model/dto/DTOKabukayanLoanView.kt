package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.Purpose
import java.math.BigDecimal


data class DTOKabuhayanLoanView(
    val applicationId:String,
    val personalInformation: DTOPersonalInformationView = DTOPersonalInformationView(),
    val companyInformation: DTOCompanyInformationView = DTOCompanyInformationView(),
    val loanInformation: DTOLoanInformationView = DTOLoanInformationView(),
    val questionnaires: List<DTOQuestionnaireView> = mutableListOf(DTOQuestionnaireView()),
    var signature:String? = null,
    val channel:DTOChannelInformationView,
)

data class DTOPersonalInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val mobile:String? = null,
    val email:String? = null,
)

data class DTOCompanyInformationView(
    val name:String? = null,
    val nature:String? = null,
    val services:String? = null,
    val operatingYears:Int? = null,
    val address: DTOAddressInformationView = DTOAddressInformationView(),
)

data class DTOLoanInformationView(
    val amount:BigDecimal? = null,
    val term:LoanTermType? = null,
    val purpose:List<String>? = mutableListOf(),
)

data class DTOQuestionnaireView(
    val question:String? = null,
    val answer:Boolean? = null,
)

