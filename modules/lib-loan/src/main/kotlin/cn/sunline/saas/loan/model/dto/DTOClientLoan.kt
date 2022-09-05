package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.EmploymentSubType
import cn.sunline.saas.loan.model.enum.EmploymentType
import cn.sunline.saas.loan.model.enum.Ownership
import java.math.BigDecimal

data class DTOClientLoan(
    val applicationId:String,
    val cifNumber:String? = null,
    val customerInformation: DTOCustomerInformation? = null,
    val financialInformation: ClientFinancialInformation? = null,
    val productInformation: DTOProductInformation? = null,
    val deliverInformation:DTODeliverInformation? = null,
    val adaInformation:DTOAdaInformation? = null,
    var questionnaires: List<DTOQuestionnaire>? = null,
    var signature:String? = null,
    val channel: DTOChannelInformation,
)


data class ClientFinancialInformation(
    val stayAtCurrentHome:DTOStayInformation? = null,
    val ownership:Ownership? = null,
    val employmentType:EmploymentType? = null,
    val employmentSubType:EmploymentSubType? = null,
    val stayWithPreviousBusiness:DTOStayInformation? = null,
    val stayWithCurrentBusiness:DTOStayInformation? = null,
    val grossMonthlyIncome: BigDecimal? = null,
    val creditCards:List<DTOCreditCard>? = null,
)

