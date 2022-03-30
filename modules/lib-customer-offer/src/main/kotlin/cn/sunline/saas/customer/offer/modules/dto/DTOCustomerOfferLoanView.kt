package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType


data class DTOCustomerOfferLoanView (
        var customerOfferProcedure:DTOCustomerOfferProcedureView? = null,
        var pdpa:PDPAInformationView? = null,
        var product:DTOProductView? = null,
        val loan:DTOLoan? = null,
        val company:DTOCompany? = null,
        val contact:DTOContact? = null,
        val detail:DTODetail? = null,
        val guarantor:DTOGuarantor? = null,
        val financial:DTOFinancial? = null,
        val uploadDocument:List<DTOUploadDocument>? = null,
        val kyc:DTOKyc? = null,
)

data class DTOCustomerOfferProcedureView(
        val customerId: Long,
        val customerOfferProcess: String,  //TODO
        val employee: Long,
        var customerOfferId:Long?,
        val customerOfferProcessNextTask:String?,  //TODO
        var status:ApplyStatus?
)


data class DTOProductView(
        var productId: Long,
        var identificationCode:String? = null,
        var name:String? = null,
        var version:String? = null,
        var description:String? = null,
        var amountConfiguration: DTOAmountConfiguration? = null,
        var termConfiguration: DTOTermConfiguration? = null,
)

data class DTOAmountConfiguration(
        val maxValueRange: String?,
        val minValueRange: String?
)

data class DTOTermConfiguration(
        val maxValueRange: LoanTermType?,
        val minValueRange: LoanTermType?
)


data class PDPAInformationView (
        val personalInformation:List<PersonalInformation>,
        val corporateInformation:List<CorporateInformation>,
        val pdpaTemplateId:String
)


data class PersonalInformation(
        val key:String,
        val name:String
)

data class CorporateInformation(
        val key: String,
        val name: String
)