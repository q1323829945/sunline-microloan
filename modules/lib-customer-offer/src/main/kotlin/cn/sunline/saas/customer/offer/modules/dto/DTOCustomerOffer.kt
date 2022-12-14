package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.customer.offer.modules.OwnershipType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType


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
    val contacts:String,
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
    val nric:String,
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


data class DTOReferenceAccount(
    val account: String,
    val accountBank: String
)