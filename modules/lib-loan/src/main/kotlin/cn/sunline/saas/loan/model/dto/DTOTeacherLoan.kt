package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.*
import java.math.BigDecimal

data class DTOTeacherLoan(
    val applicationId:String,
    val loanType:TeacherLoanType? = null,
    val personalInformation: DTOTeacherPersonalInformation? = null,
    val contactInformation: DTOContactInformation? = null,
    val educationalBackground: DTOEducationalBackground? = null,
    val employmentInformation: DTOEmploymentInformation? = null,
    val financialInformation: DTOTeacherFinancialInformation? = null,
    val spouseInformation: DTOTeacherSpouseInformation? = null,
    val loanInformation: DTOLoanInformation? = null,
    val disbursalMode: DTODisbursalMode? = null,
    val characterReferences: List<DTOCharacterReferences>? = null,
    val channel: DTOChannelInformation,
    var signature:String? = null

)


data class DTOTeacherPersonalInformation(
    val name: DTONameInformation? = null,
    val gender:Gender? = null,
    val birthDay:String? = null,
    val citizenship:String? = null,
    val motherMaidenName: DTONameInformation? = null,
    val civilStatus:CivilStatus? = null,
    val age:String? = null,
    val birthPlace:String? = null,
    val numberOfDependents:Int = 0
)


data class DTOContactInformation(
    val mobile:String? = null,
    val residenceNo:String? = null,
    val officeNo:String? = null,
    val email:String? = null,
    val gsis:String? = null,
    val sss:String? = null,
    val tin:String? = null,
    val employee:String? = null,
    val presentAddress: DTOAddressInformation? = null,
    val permanentAddress: DTOAddressInformation? = null,
)

data class DTOEducationalBackground(
    val educationalAttainment:EducationalAttainment? = null,
    val lastSchoolAttended:String? = null,
)

data class DTOEmploymentInformation(
    val employer:String? = null,
    val address: DTOAddressInformation? = null,
    val designation:String? = null,
    val industryExpYear:Int? = null,
    val depEdRegion:String? = null,
    val depEdDivision:String? = null,
    val depEdStation:String? = null,
    val employmentStatus:EmploymentStatus? = null,
    val email:String? = null,
)

data class DTOTeacherFinancialInformation(
    val revenueDetails: DTORevenueInformation? = null,
    val creditCardInformation: DTOCreditCardInformation? = null,
    val sourceOfFunds:String? = null,
    val existingBDONetworkBankAccount:String? = null,
    val existingBDOAccount:String? = null,
    val otherBankAccount:String? = null,

    )

data class DTORevenueInformation(
    val sourceOfIncome:List<DTOSourceOfIncome>? = null,
    val totalIncome: DTOTotalIncome? = null,

    )

data class DTOSourceOfIncome(
    val incomeType:IncomeType? = null,
    val source:String? = null,
    val grossAmount:BigDecimal? = null,
    val netAmount:BigDecimal? = null,
    val frequency:String? = null,
)

data class DTOTotalIncome(
    val grossAmount:BigDecimal? = null,
    val netAmount:BigDecimal? = null,
)

data class DTOCreditCardInformation(
    val creditCard:String? = null,
    val creditLimit:BigDecimal? = null,
)

data class DTOTeacherSpouseInformation(
    val name: DTONameInformation? = null,
    val birthDay:String? = null,
    val birthPlace:String? = null,
    val occupation:String? = null,
    val mobile:String? = null,
    val employer:String? = null,
    val employerAddress: DTOAddressInformation? = null,
    val employerTel:String? = null,
    val grossIncome:BigDecimal? = null,
)

data class DTODisbursalMode(
    val networkBankCASA:String? = null,
    val casa:String? = null,
    val payrollAccount:String? = null,
    val others:String? = null,
)

data class DTOCharacterReferences(
    val name: DTONameInformation? = null,
    val contact:String? = null,
    val relationship:String? = null,
)