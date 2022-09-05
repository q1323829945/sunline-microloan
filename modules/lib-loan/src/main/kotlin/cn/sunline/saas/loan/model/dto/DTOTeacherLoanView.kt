package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTOTeacherLoanView(
    val applicationId:String,
    val loanType:TeacherLoanType? = null,
    val personalInformation: DTOTeacherPersonalInformationView = DTOTeacherPersonalInformationView(),
    val contactInformation: DTOContactInformationView = DTOContactInformationView(),
    val educationalBackground: DTOEducationalBackgroundView = DTOEducationalBackgroundView(),
    val employmentInformation: DTOEmploymentInformationView = DTOEmploymentInformationView(),
    val financialInformation: DTOTeacherFinancialInformationView = DTOTeacherFinancialInformationView(),
    val spouseInformation: DTOTeacherSpouseInformationView = DTOTeacherSpouseInformationView(),
    val loanInformation: DTOLoanInformationView = DTOLoanInformationView(),
    val disbursalMode: DTODisbursalModeView = DTODisbursalModeView(),
    val characterReferences: List<DTOCharacterReferencesView> = mutableListOf(DTOCharacterReferencesView()),
    val channel: DTOChannelInformationView,
    var signature:String? = null

)


data class DTOTeacherPersonalInformationView(
    val name: DTONameInformationView? = DTONameInformationView(),
    val gender:Gender? = null,
    val birthDay:String? = null,
    val citizenship:String? = null,
    val motherMaidenName: DTONameInformationView = DTONameInformationView(),
    val civilStatus:CivilStatus? = null,
    val age:String? = null,
    val birthPlace:String? = null,
    val numberOfDependents:Int = 0
)


data class DTOContactInformationView(
    val mobile:String? = null,
    val residenceNo:String? = null,
    val officeNo:String? = null,
    val email:String? = null,
    val gsis:String? = null,
    val sss:String? = null,
    val tin:String? = null,
    val employee:String? = null,
    val presentAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val permanentAddress: DTOAddressInformationView = DTOAddressInformationView(),
)

data class DTOEducationalBackgroundView(
    val educationalAttainment:String? = null,
    val lastSchoolAttended:String? = null,
)

data class DTOEmploymentInformationView(
    val employer:String? = null,
    val address: DTOAddressInformationView = DTOAddressInformationView(),
    val designation:String? = null,
    val industryExpYear:Int? = null,
    val depEdRegion:String? = null,
    val depEdDivision:String? = null,
    val depEdStation:String? = null,
    val employmentStatus:EmploymentStatus? = null,
    val email:String? = null,
)

data class DTOTeacherFinancialInformationView(
    val revenueDetails: DTORevenueInformationView = DTORevenueInformationView(),
    val creditCardInformation: DTOCreditCardInformationView = DTOCreditCardInformationView(),
    val sourceOfFunds:String? = null,
    val existingBDONetworkBankAccount:String? = null,
    val existingBDOAccount:String? = null,
    val otherBankAccount:String? = null,

    )

data class DTORevenueInformationView(
    val sourceOfIncome:List<DTOSourceOfIncomeView> = mutableListOf(DTOSourceOfIncomeView()),
    val totalIncome: DTOTotalIncome? = DTOTotalIncome(),

    )

data class DTOSourceOfIncomeView(
    val incomeType:IncomeType? = null,
    val source:String? = null,
    val grossAmount:BigDecimal? = null,
    val netAmount:BigDecimal? = null,
    val frequency:String? = null,
)

data class DTOTotalIncomeView(
    val grossAmount:BigDecimal? = null,
    val netAmount:BigDecimal? = null,
)

data class DTOCreditCardInformationView(
    val creditCard:String? = null,
    val creditLimit:BigDecimal? = null,
)

data class DTOTeacherSpouseInformationView(
    val name: DTONameInformationView = DTONameInformationView(),
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val occupation:String? = null,
    val mobile:String? = null,
    val employer:String? = null,
    val employerAddress: DTOAddressInformationView = DTOAddressInformationView(),
    val employerTel:String? = null,
    val grossIncome:BigDecimal? = null,
)

data class DTODisbursalModeView(
    val networkBankCASA:String? = null,
    val casa:String? = null,
    val payrollAccount:String? = null,
    val others:String? = null,
)

data class DTOCharacterReferencesView(
    val name: DTONameInformationView = DTONameInformationView(),
    val contact:String? = null,
    val relationship:String? = null,
)