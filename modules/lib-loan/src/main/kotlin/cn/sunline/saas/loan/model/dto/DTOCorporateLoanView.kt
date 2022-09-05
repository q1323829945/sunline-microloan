package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTOCorporateLoanView(
    val applicationId:String,
    val borrowerType:BorrowerType? = null,
    val program:String? = null,
    val referralInformation: DTOReferralInformationView = DTOReferralInformationView(),
    val borrowerInformation: DTOBorrowerInformationView = DTOBorrowerInformationView(),
    val partnersInformation: List<DTOPartnersInformationView> = mutableListOf(DTOPartnersInformationView()),
    val mortgagorInformation: DTOMortgagorInformationView = DTOMortgagorInformationView(),
    val spouseInformation: DTOSpouseInformationView = DTOSpouseInformationView(),
    val loanInformation: DTOCorporateLoanInformationView = DTOCorporateLoanInformationView(),
    val collateralInformation: DTOCollateralInformationView = DTOCollateralInformationView(),
    val financialInformation: DTOCorporateFinancialInformationView = DTOCorporateFinancialInformationView(),
    val tradeReferences: DTOTradeReferencesView = DTOTradeReferencesView(),
    val undertaking:DTOUndertakingView = DTOUndertakingView(),
    val channel: DTOChannelInformationView
)

data class DTOReferralInformationView(
    val branch:String? = null,
    val developer:String? = null,
    val referrer:String? = null,
    val accountOfficer:String? = null,
    val others:String? = null,
)

data class DTOBorrowerInformationView(
    val name:String? = null,
    val nature:String? = null,
    val operatingYears:Int? = null,
    val businessType:BusinessType? = null,
    val registration:String? = null,
    val tin:String? = null,
    val sss:String? = null,
    val businessAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val factoryAddress: DTOAddressInformationView = DTOAddressInformationView(),
)

data class DTOPartnersInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val position:String? = null,
    val stockRight:String? = null,
    val birthDay:String? = null,
)

data class DTOMortgagorInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val gender:Gender? = null,
    val civilStatus:CivilStatus? = null,
    val citizenship:String? = null,
    val motherName: DTONameInformationView = DTONameInformationView(),
    val fatherName: DTONameInformationView = DTONameInformationView(),
    val tin:String? = null,
    val sss:String? = null,
    val mobile:String? = null,
    val paidType:PaidType? = null,
    val residencePhone: DTOPhoneInformationView = DTOPhoneInformationView(),
    val officePhone: DTOPhoneInformationView = DTOPhoneInformationView(),
    val fax: DTOPhoneInformationView = DTOPhoneInformationView(),
    val email:String? = null,
    val presentAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val permanentAddress: DTOAddressInformationView = DTOAddressInformationView(),
)


data class DTOSpouseInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val citizenship:String? = null,
    val tin:String? = null,
    val sssOrGsis: DTOIDInformationView = DTOIDInformationView(),
    val officePhone:DTOPhoneInformationView = DTOPhoneInformationView(),
    val mobile:String? = null,
    val paidType:PaidType? = null,
    val email:String? = null,
)

data class DTOCorporateLoanInformationView(
    val amount: BigDecimal? = null,
    val term:LoanTermType? = null,
    val fixingPeriod:String? = null,
    val purpose: DTOPurposeInformationView = DTOPurposeInformationView(),
)


data class DTOPurposeInformationView(
    val workingCapital:List<WorkingCapital> = mutableListOf(),
    val capex:List<Capex> = mutableListOf(),
    val investment:List<Investment> = mutableListOf(),
)


data class DTOCollateralInformationView(
    val propertyAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val presentRegisteredOwner:String? = null,
    val cct:String? = null,
    val contactPerson:String? = null,
    val contactNumber:String? = null,
    val collateralType:CollateralType? = null,
    val collateralUse:CollateralUse? = null,
)

data class DTOCorporateFinancialInformationView(
    val deposits:List<DTODepositsInformationView> = mutableListOf(DTODepositsInformationView()),
    val loans:List<DTOLoansInformationView> = mutableListOf(DTOLoansInformationView()),
)

data class DTODepositsInformationView(
    val bank:String? = null,
    val type:String? = null,
    val account:String? = null,
    val openedDate:String? = null,
    val outstandingBalance:BigDecimal? = null,
    val depositor:String? = null
)

data class DTOLoansInformationView(
    val bank:String? = null,
    val type:String? = null,
    val amount:BigDecimal? = null,
    val grantedDate:String? = null,
    val maturityDate:String? = null,
    val monthlyPayment:BigDecimal? = null,
)

data class DTOTradeReferencesView(
    val majorCustomers:List<DTOMajorCustomerInformationView> = mutableListOf(DTOMajorCustomerInformationView()),
    val majorSuppliers:List<DTOMajorSupplierInformationView> = mutableListOf(DTOMajorSupplierInformationView()),

    )

data class DTOMajorCustomerInformationView(
    val companyName:String? = null,
    val contactName:String? = null,
    val contactNumber:String? = null,
)

data class DTOMajorSupplierInformationView(
    val companyName:String? = null,
    val contactName:String? = null,
    val contactNumber:String? = null,
)

data class DTOUndertakingView(
    val account:String? = null,
    val appraisalFees:BigDecimal? = null,
    var signature:String? = null,
)

data class DTOIDInformationView(
    val type:String? = null,
    val number:String? = null,
)
