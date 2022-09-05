package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTONewClientLoanView(
    val applicationId:String,
    val cif: CIFView = CIFView(),
    val customerInformation: DTOCustomerInformationView = DTOCustomerInformationView(),
    val contactInformation: ClientContactInformationView = ClientContactInformationView(),
    val personalInformation: ClientPersonalInformationView = ClientPersonalInformationView(),
    val financialInformation: NewClientFinancialInformationView = NewClientFinancialInformationView(),
    val dataPrivacyConsent: DTOSignatureView = DTOSignatureView(),
    val consentForTheIssuance: DTOSignatureView = DTOSignatureView(),
    val customerUndertaking: DTOSignatureView = DTOSignatureView(),
    val internal: DTOInternalView = DTOInternalView(),
    val productInformation:DTOProductInformationView = DTOProductInformationView(),
    val deliverInformation:DTODeliverInformationView = DTODeliverInformationView(),
    val adaInformation:DTOAdaInformationView = DTOAdaInformationView(),
    val questionnaires: List<DTOQuestionnaireView> = mutableListOf(DTOQuestionnaireView()),
    var signature:String? = null,
    val channel: DTOChannelInformationView,
)

data class CIFView(
    val number:String? = null,
    val type:CIFType? = null,
)

data class DTOCustomerInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val birthDay:String? = null,
    val isBDOCustomer:Boolean? = null,
    val bdoProductType:BDOProductType? = null,
)


data class ClientContactInformationView(
    val mobileInformation: DTOPhoneInformationView = DTOPhoneInformationView(),
    val personalEmail:String? = null,
    val homeLandline: DTOPhoneInformationView = DTOPhoneInformationView(),
    val homeAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val alternateAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val workEmail:String? = null,
    val workLandline: DTOPhoneInformationView = DTOPhoneInformationView(),
    val workAddress: DTOAddressInformationView = DTOAddressInformationView()
)

data class ClientPersonalInformationView(
    val birthCountry:String? = null,
    val gender:Gender? = null,
    val civilStatus:CivilStatus? = null,
    val citizenship:String? = null,
    val tin:String? = null,
)


data class NewClientFinancialInformationView(
    val fundsSource:List<FundsSource> = mutableListOf(),
    val remittanceCountry:String? = null,
    val primarilyUse:String? = null,
    val natures:List<Natures> = mutableListOf(),
    val name:String? = null,
    val position: DTOPositionView = DTOPositionView(),
    val grossMonthlyIncome:BigDecimal? = null,
    val stayAtCurrentHome:DTOStayInformationView = DTOStayInformationView(),
    val ownership:Ownership? = null,
    val employmentType:EmploymentType? = null,
    val employmentSubType:EmploymentSubType? = null,
    val stayWithPreviousBusiness:DTOStayInformationView = DTOStayInformationView(),
    val stayWithCurrentBusiness:DTOStayInformationView = DTOStayInformationView(),
    val creditCards:List<DTOCreditCardView> = mutableListOf(DTOCreditCardView()),
)

data class DTOCreditCardView(
    val bank:String? = null,
    val lastSixDigits:String? = null,
    val year:String? = null,
)

data class DTOPositionView(
    val positionType:PositionType? = null,
    val positionSubType:PositionSubType? = null,
)

data class DTOSignatureView(
    var signature:String? = null,
)
data class DTOInternalView(
    val account:String? = null,
    val openedDate:String? = null,
    val residency:Residency? = null,
    val biometrics:List<Biometrics>? = mutableListOf(),
    val rc:RC? = null,
    val nlds:Boolean? = null,
    val id1: DTOClientIDView = DTOClientIDView(),
    val id2: DTOClientIDView = DTOClientIDView(),
    val referred:String? = null,
    val verified:String? = null,
    val approved:String? = null,
    val remark:String? = null,
)

data class DTOClientIDView(
    val type:String? = null,
    val number:String? = null,
)

data class DTOProductInformationView(
    val productType:PersonalProductType? = null,
    val creditCardDetails:DTOCreditCardDetailsView = DTOCreditCardDetailsView(),
    val details:DTOProductDetailsView = DTOProductDetailsView(),
)

data class DTOCreditCardDetailsView(
    val creditCardType:CreditCardType? = null,
    val creditCardSubType:CreditCardSubType? = null,
    val virtualCard:Boolean? = null,
)

data class DTOProductDetailsView(
    val amount:BigDecimal? = null,
    val term:LoanTermType? = null,
    val purpose:String? = null,
)

data class DTODeliverInformationView(
    val deliverAddress:DeliverAddress? = null,
    val deliverEmail:DeliverEmail? = null,
)

data class DTOAdaInformationView(
    val newAccount:Boolean? = null,
    val accountInformation:DTOAccountInformationView = DTOAccountInformationView()
)

data class DTOAccountInformationView(
    val bdoAccount:String? = null,
    val primarySignatory:String? = null,
    val secondarySignatory:String? = null,
)