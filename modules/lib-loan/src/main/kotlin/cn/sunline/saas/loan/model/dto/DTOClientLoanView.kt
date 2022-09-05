package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.EmploymentSubType
import cn.sunline.saas.loan.model.enum.EmploymentType
import cn.sunline.saas.loan.model.enum.Ownership
import java.math.BigDecimal

data class DTOClientLoanView(
    val applicationId:String,
    val cifNumber:String? = null,
    val customerInformation: DTOCustomerInformationView = DTOCustomerInformationView(),
    val financialInformation: ClientFinancialInformationView = ClientFinancialInformationView(),
    val productInformation: DTOProductInformationView = DTOProductInformationView(),
    val deliverInformation:DTODeliverInformationView = DTODeliverInformationView(),
    val adaInformation:DTOAdaInformationView = DTOAdaInformationView(),
    val questionnaires: List<DTOQuestionnaireView> = mutableListOf(DTOQuestionnaireView()),
    var signature:String? = null,
    val channel: DTOChannelInformationView,
)


data class ClientFinancialInformationView(
    val stayAtCurrentHome:DTOStayInformationView = DTOStayInformationView(),
    val ownership:Ownership? = null,
    val employmentType:EmploymentType? = null,
    val employmentSubType:EmploymentSubType? = null,
    val stayWithPreviousBusiness:DTOStayInformationView = DTOStayInformationView(),
    val stayWithCurrentBusiness:DTOStayInformationView =  DTOStayInformationView(),
    val grossMonthlyIncome: BigDecimal? = null,
    val creditCards:List<DTOCreditCardView> = mutableListOf(DTOCreditCardView()),
)

