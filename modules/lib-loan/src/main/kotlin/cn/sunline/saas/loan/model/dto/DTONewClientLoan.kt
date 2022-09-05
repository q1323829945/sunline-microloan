package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTONewClientLoan(
    val applicationId:String,
    val cif: CIF? = null,
    val customerInformation: DTOCustomerInformation? = null,
    val contactInformation: ClientContactInformation? = null,
    val personalInformation: ClientPersonalInformation? = null,
    val financialInformation: NewClientFinancialInformation? = null,
    val dataPrivacyConsent: DTOSignature? = null,
    val consentForTheIssuance: DTOSignature? = null,
    val customerUndertaking: DTOSignature? = null,
    val internal: DTOInternal? = null,
    val productInformation:DTOProductInformation? = null,
    val deliverInformation:DTODeliverInformation? = null,
    val adaInformation:DTOAdaInformation? = null,
    var questionnaires: List<DTOQuestionnaire>? = null,
    var signature:String? = null,
    val channel: DTOChannelInformation,
)

data class CIF(
    val number:String? = null,
    val type:CIFType? = null,
)

data class DTOCustomerInformation(
    val name: DTONameInformation? = null,
    val birthDay:String? = null,
    val isBDOCustomer:Boolean? = null,
    val bdoProductType:BDOProductType? = null,
)


data class ClientContactInformation(
    val mobileInformation: DTOPhoneInformation? = null,
    val personalEmail:String? = null,
    val homeLandline: DTOPhoneInformation? = null,
    val homeAddress: DTOAddressInformation? = null,
    val alternateAddress: DTOAddressInformation? = null,
    val workEmail:String? = null,
    val workLandline: DTOPhoneInformation? = null,
    val workAddress: DTOAddressInformation? = null,
)

data class ClientPersonalInformation(
    val birthCountry:String? = null,
    val gender:Gender? = null,
    val civilStatus:CivilStatus? = null,
    val citizenship:String? = null,
    val tin:String? = null,
)


data class NewClientFinancialInformation(
    val fundsSource:List<FundsSource>? = null,
    val remittanceCountry:String? = null,
    val primarilyUse:String? = null,
    val natures:List<Natures>? = null,
    val name:String? = null,
    val position: DTOPosition? = null,
    val grossMonthlyIncome:BigDecimal? = null,
    val stayAtCurrentHome:DTOStayInformation? = null,
    val ownership:Ownership? = null,
    val employmentType:EmploymentType? = null,
    val employmentSubType:EmploymentSubType? = null,
    val stayWithPreviousBusiness:DTOStayInformation? = null,
    val stayWithCurrentBusiness:DTOStayInformation? = null,
    val creditCards:List<DTOCreditCard>? = null,
)

data class DTOCreditCard(
    val bank:String? = null,
    val lastSixDigits:String? = null,
    val year:String? = null,
)

data class DTOPosition(
    val positionType:PositionType? = null,
    val positionSubType:PositionSubType? = null,
)

data class DTOSignature(
    var signature:String? = null,
)
data class DTOInternal(
    val account:String? = null,
    val openedDate:String? = null,
    val residency:Residency? = null,
    val biometrics:List<Biometrics>? = null,
    val rc:RC? = null,
    val nlds:Boolean? = null,
    val id1: DTOClientID? = null,
    val id2: DTOClientID? = null,
    val referred:String? = null,
    val verified:String? = null,
    val approved:String? = null,
    val remark:String? = null,
)

data class DTOClientID(
    val type:String? = null,
    val number:String? = null,
)

data class DTOProductInformation(
    val productType:PersonalProductType? = null,
    val creditCardDetails:DTOCreditCardDetails? = null,
    val details:DTOProductDetails? = null,
)

data class DTOCreditCardDetails(
    val creditCardType:CreditCardType? = null,
    val creditCardSubType:CreditCardSubType? = null,
    val virtualCard:Boolean? = null,
)

data class DTOProductDetails(
    val amount:BigDecimal? = null,
    val term:LoanTermType? = null,
    val purpose:String? = null,
)

data class DTODeliverInformation(
    val deliverAddress:DeliverAddress? = null,
    val deliverEmail:DeliverEmail? = null,
)

data class DTOAdaInformation(
    val newAccount:Boolean? = null,
    val accountInformation:DTOAccountInformation? = null,
)

data class DTOAccountInformation(
    val bdoAccount:String? = null,
    val primarySignatory:String? = null,
    val secondarySignatory:String? = null,
)