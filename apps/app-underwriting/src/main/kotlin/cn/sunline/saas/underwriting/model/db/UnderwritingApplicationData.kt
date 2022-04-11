package cn.sunline.saas.underwriting.model.db

import cn.sunline.saas.global.constant.LoanTermType

/**
 * @title: DTOUnderwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:31
 */
data class UnderwritingApplicationData (
    val applId:Long,
    val requestId:String,
    val loan:DTOLoan,
    val detail:DTODetail,
    val guarantor:DTOGuarantor,
    val financial:DTOFinancial,
    val kyc:DTOKyc,
)

data class DTOLoan(
    val amount:String,
    val currency: CurrencyType,
    val term: LoanTermType,
    val local: YesOrNo,
    val employ:String,
)

data class DTODetail(
    val customerId:Long,
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
    val nationality: Country,
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
    val mainAccountWithOurBank:YesOrNo,
    val outLoanNotWithOutBank:YesOrNo,
)

data class DTOKyc(
    val businessInBlackListArea:YesOrNo,
    val businessPlanInBlackListArea:YesOrNo,
    val businessOrPartnerSanctioned:YesOrNo,
    val relationInBlackListArea:YesOrNo,
    val repaymentSourceInBlackListArea:YesOrNo,
    val representsNeutrality:YesOrNo,
    val representsNeutralityShared:YesOrNo,
    val familiarWithBusiness:String ,//TODO
)
