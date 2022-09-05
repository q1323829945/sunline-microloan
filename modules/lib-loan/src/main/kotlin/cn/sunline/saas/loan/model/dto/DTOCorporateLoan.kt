package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTOCorporateLoan(
    val applicationId:String,
    val borrowerType:BorrowerType? = null,
    val program:String? = null,
    val referralInformation: DTOReferralInformation? = null,
    val borrowerInformation: DTOBorrowerInformation? = null,
    val partnersInformation: List<DTOPartnersInformation>? = null,
    val mortgagorInformation: DTOMortgagorInformation? = null,
    val spouseInformation: DTOSpouseInformation? = null,
    val loanInformation: DTOCorporateLoanInformation? = null,
    val collateralInformation: DTOCollateralInformation? = null,
    val financialInformation: DTOCorporateFinancialInformation? = null,
    val tradeReferences: DTOTradeReferences? = null,
    val undertaking:DTOUndertaking? = null,
    val channel: DTOChannelInformation
)

data class DTOReferralInformation(
    val branch:String? = null,
    val developer:String? = null,
    val referrer:String? = null,
    val accountOfficer:String? = null,
    val others:String? = null,
)

data class DTOBorrowerInformation(
    val name:String? = null,
    val nature:String? = null,
    val operatingYears:Int? = null,
    val businessType:BusinessType? = null,
    val registration:String? = null,
    val tin:String? = null,
    val sss:String? = null,
    val businessAddress: DTOAddressInformation? = null,
    val factoryAddress: DTOAddressInformation? = null,
)

data class DTOPartnersInformation(
    val name: DTONameInformation? = null,
    val position:String? = null,
    val stockRight:String? = null,
    val birthDay:String? = null,
)

data class DTOMortgagorInformation(
    val name: DTONameInformation? = null,
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val gender:Gender? = null,
    val civilStatus:CivilStatus? = null,
    val citizenship:String? = null,
    val motherName: DTONameInformation? = null,
    val fatherName: DTONameInformation? = null,
    val tin:String? = null,
    val sss:String? = null,
    val mobile:String? = null,
    val paidType:PaidType? = null,
    val residencePhone: DTOPhoneInformation? = null,
    val officePhone: DTOPhoneInformation? = null,
    val fax: DTOPhoneInformation? = null,
    val email:String? = null,
    val presentAddress: DTOAddressInformation? = null,
    val permanentAddress: DTOAddressInformation? = null,
)


data class DTOSpouseInformation(
    val name: DTONameInformation? = null,
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val citizenship:String? = null,
    val tin:String? = null,
    val sssOrGsis: DTOIDInformation? = null,
    val officePhone:DTOPhoneInformation? = null,
    val mobile:String? = null,
    val paidType:PaidType? = null,
    val email:String? = null,
)

data class DTOCorporateLoanInformation(
    val amount: BigDecimal? = null,
    val term:LoanTermType? = null,
    val fixingPeriod:String? = null,
    val purpose: DTOPurposeInformation? = null,
)


data class DTOPurposeInformation(
    val workingCapital:List<WorkingCapital>? = null,
    val capex:List<Capex>? = null,
    val investment:List<Investment>? = null,
)


data class DTOCollateralInformation(
    val propertyAddress: DTOAddressInformation? = null,
    val presentRegisteredOwner:String? = null,
    val cct:String? = null,
    val contactPerson:String? = null,
    val contactNumber:String? = null,
    val collateralType:CollateralType? = null,
    val collateralUse:CollateralUse? = null,
)

data class DTOCorporateFinancialInformation(
    val deposits:List<DTODepositsInformation>? = null,
    val loans:List<DTOLoansInformation>? = null,
)

data class DTODepositsInformation(
    val bank:String? = null,
    val type:String? = null,
    val account:String? = null,
    val openedDate:String? = null,
    val outstandingBalance:BigDecimal? = null,
    val depositor:String? = null
)

data class DTOLoansInformation(
    val bank:String? = null,
    val type:String? = null,
    val amount:BigDecimal? = null,
    val grantedDate:String? = null,
    val maturityDate:String? = null,
    val monthlyPayment:BigDecimal? = null,
)

data class DTOTradeReferences(
    val majorCustomers:List<DTOMajorCustomerInformation>? = null,
    val majorSuppliers:List<DTOMajorSupplierInformation>? = null,

    )

data class DTOMajorCustomerInformation(
    val companyName:String? = null,
    val contactName:String? = null,
    val contactNumber:String? = null,
)

data class DTOMajorSupplierInformation(
    val companyName:String? = null,
    val contactName:String? = null,
    val contactNumber:String? = null,
)

data class DTOUndertaking(
    val account:String? = null,
    val appraisalFees:BigDecimal? = null,
    var signature:String? = null,
)

data class DTOIDInformation(
    val type:String? = null,
    val number:String? = null,
)
