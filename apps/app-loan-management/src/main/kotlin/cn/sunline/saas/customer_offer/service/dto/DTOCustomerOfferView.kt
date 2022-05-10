package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer.offer.modules.OwnershipType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType

data class DTOManagementCustomerOfferView(
    var customerOfferProcedure: DTOCustomerOfferProcedureView? = null,
    var pdpa: PDPAInformationView? = null,
    var product: DTOProductView? = null,
    val loan: DTOLoan? = null,
    val company: DTOCompany? = null,
    val contact: DTOContact? = null,
    val detail: DTODetail? = null,
    val guarantor: DTOGuarantor? = null,
    val financial: DTOFinancial? = null,
    val uploadDocument:List<DTOUploadDocument>? = null,
    val kyc: DTOKyc? = null,
)

data class DTOCustomerOfferProcedureView(
    val customerId: Long,
    val customerOfferProcess: String,  //TODO
    val employee: Long,
    var customerOfferId:Long?,
    val customerOfferProcessNextTask:String?,  //TODO
    var status: ApplyStatus?
)

data class PDPAInformationView (
    val personalInformation:List<PersonalInformationView>,
    val corporateInformation:List<CorporateInformationView>,
    val pdpaTemplateId:String
)


data class PersonalInformationView(
    val key:String,
    val name:String
)

data class CorporateInformationView(
    val key: String,
    val name: String
)


data class DTOProductView(
    var productId: Long,
    var identificationCode:String? = null,
    var name:String? = null,
    var version:String? = null,
    var description:String? = null,
    var amountConfiguration: DTOAmountConfigurationView? = null,
    var termConfiguration: DTOTermConfigurationView? = null,
)

data class DTOAmountConfigurationView(
    val maxValueRange: String?,
    val minValueRange: String?
)

data class DTOTermConfigurationView(
    val maxValueRange: LoanTermType?,
    val minValueRange: LoanTermType?
)



data class DTOLoan(
    val amount:String,
    val currency: CurrencyType,
    val term: LoanTermType,
    val local: YesOrNo,
    val employ:String,
)

data class DTOCompany(
    val registrationNo:String
)

data class DTOContact(
    val contactNRIC:String,
    val mobileArea:String,
    val mobileNumber:String,
    val email:String,
)

data class DTODetail(
    val name:String,
    val registrationNo:String,
    val address:String,
    val businessType:String, //TODO
    val contactAddress:String,
    val businessPremiseType: OwnershipType,
    val businessFocused:Long,
)

data class DTOGuarantor(
    val primaryGuarantor:String,
    val guarantors:List<DTOGuarantors>
)

data class DTOGuarantors(
    val name:String,
    val NRIC:String,
    val nationality: CountryType,
    val mobileArea:String,
    val mobileNumber:String,
    val email:String,
    val occupation:String, //TODO
    val industryExpYear:Long,
    val manageExpYear:Long,
    val residenceType:String,//TODO 住宅类型
    val residenceOwnership: OwnershipType,
)

data class DTOFinancial(
    val lastestYearRevenus:String,
    val mainAccountWithOurBank: YesOrNo,
    val outLoanNotWithOutBank: YesOrNo,
)

data class DTOUploadDocument(
    val documentTemplateId:String,
    val documentTemplateName:String,
    var fileName:String?,
    var file:String?,
)

data class DTOKyc(
    val businessInBlackListArea: YesOrNo,
    val businessPlanInBlackListArea: YesOrNo,
    val businessOrPartnerSanctioned: YesOrNo,
    val relationInBlackListArea: YesOrNo,
    val repaymentSourceInBlackListArea: YesOrNo,
    val representsNeutrality: YesOrNo,
    val representsNeutralityShared: YesOrNo,
    val familiarWithBusiness:String,//TODO
)