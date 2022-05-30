package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.OwnershipType
import cn.sunline.saas.customer.offer.modules.dto.DTOReferenceAccount
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.underwriting.controllers.dto.DTOLoanApplicationData

data class DTOManagementCustomerOfferView(
    var pdpa: PDPAInformationView? = null,
    var product: DTOProductView? = null,
    val loan: DTOLoanView? = null,
    val company: DTOCompanyView? = null,
    val contact: DTOContactView? = null,
    val detail: DTODetailView? = null,
    val guarantor: DTOGuarantorView? = null,
    val financial: DTOFinancialView? = null,
    val uploadDocument:List<DTOUploadDocumentView>? = null,
    val kyc: DTOKycView? = null,
    var underwriting:DTOUnderwritingView? = null,
    val referenceAccount: DTOReferenceAccountView? = null
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
    var productId: String? = null,
    var identificationCode:String? = null,
    var name:String? = null,
    var version:String? = null,
    var description:String? = null,
    var loanPurpose: String? = null,
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



data class DTOLoanView(
    val amount:String,
    val currency: CurrencyType,
    val term: LoanTermType,
    val local: YesOrNo,
    val employ:String,
)

data class DTOCompanyView(
    val registrationNo:String
)

data class DTOContactView(
    val contacts:String,
    val contactNRIC:String,
    val mobileArea:String,
    val mobileNumber:String,
    val email:String,
)

data class DTODetailView(
    val name:String,
    val registrationNo:String,
    val address:String,
    val businessType:String, //TODO
    val contactAddress:String,
    val businessPremiseType: OwnershipType,
    val businessFocused:Long,
)

data class DTOGuarantorView(
    val primaryGuarantor:String,
    val guarantors:List<DTOGuarantorsView>
)

data class DTOGuarantorsView(
    val name:String,
    val nric:String,
    val nationality: CountryType,
    val mobileArea:String,
    val mobileNumber:String,
    val email:String,
    val occupation:String, //TODO
    val industryExpYear:Long,
    val manageExpYear:Long,
    val residenceType:String,//TODO ×¡Õ¬ÀàÐÍ
    val residenceOwnership: OwnershipType,
)

data class DTOFinancialView(
    val lastestYearRevenus:String,
    val mainAccountWithOurBank: YesOrNo,
    val outLoanNotWithOutBank: YesOrNo,
)

data class DTOUploadDocumentView(
    val documentTemplateId:String,
    var documentTemplateName:String?,
    var fileName:String?,
    val file:String,
)

data class DTOKycView(
    val businessInBlackListArea: YesOrNo,
    val businessPlanInBlackListArea: YesOrNo,
    val businessOrPartnerSanctioned: YesOrNo,
    val relationInBlackListArea: YesOrNo,
    val repaymentSourceInBlackListArea: YesOrNo,
    val representsNeutrality: YesOrNo,
    val representsNeutralityShared: YesOrNo,
    val familiarWithBusiness:String,//TODO
)


data class DTOUnderwritingView (
    val id: String,
    val customerId: String,
    val applicationData: DTOLoanApplicationData,
    var customerCreditRate: String? = null,
    var creditRisk: String? = null,
    var fraudEvaluation: String? = null,
    var regulatoryCompliance: String? = null,
    val status: String?
)
data class DTOReferenceAccountView(
    val account: String,
    val accountBank: String
)