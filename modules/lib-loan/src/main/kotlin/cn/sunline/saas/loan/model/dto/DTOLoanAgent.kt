package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.model.enum.*

data class DTOLoanAgent(
    val seq:String? = null,
    var applicationId:String? = null,
    val personalInformation:DTOBasePersonalInformation? = null,
    val contactInformation:DTOBaseContactInformation? = null,
    val employeeInformation:DTOBaseEmployeeInformation? = null,
    val corporateInformation:DTOBaseCorporateInformation? = null,
    val loanType:LoanType? = null,
    val fileInformation:List<DTOBaseFileInformation> ? = null,
    val signature:String? = null,
    var productId:String? = null,
    var productName: String? = null,
    var productType: ProductType? = null,
    val channel: DTOChannelInformation,
)


data class DTOBasePersonalInformation(
    val name:DTONameInformation? = null,
    val gender:Gender? = null,
    val birthDay:String? = null,
    val tin:String? = null,
    val sss:String? = null,
    val citizenship:String? = null,
    val civilStatus:CivilStatus? = null,
    val numberOfDependents:Int = 0,
    val spouseInformation:DTOBaseSpouseInformation? = null,
    val educationalBackground:DTOEducationalBackground? = null,

)

data class DTOBaseSpouseInformation(
    val name:DTONameInformation? = null,
)

data class DTOBaseContactInformation(
    val mobile:DTOPhoneInformation? = null,
    val email:String? = null,
    val homeLandLine:DTOPhoneInformation? = null,
    val address:DTOAddressInformation? = null,
    val billingContact:DTOBaseBillingContact? = null,
)

data class DTOBaseBillingContact(
    val name:DTONameInformation? = null,
    val mobile:DTOPhoneInformation? = null,
    val email:String? = null,
    val homeLandLine:DTOPhoneInformation? = null,
)

data class DTOBaseEmployeeInformation(
    val employeeType:EmploymentType? = null,
    val name:String? = null,
    val address:DTOAddressInformation? = null,
    val mobile:DTOPhoneInformation? = null,
    val email:String? = null,
    val homeLandLine:DTOPhoneInformation? = null,
)

data class DTOBaseCorporateInformation(
    val name:String? = null,
    val nature:String? = null,
    val businessType:BusinessType? = null,
    val registrationNo:String? = null,
    val tin:String? = null,
    val sss:String? = null,
    val address:DTOAddressInformation? = null,
    val mobile:DTOPhoneInformation? = null,
    val email:String? = null,
    val homeLandLine:DTOPhoneInformation? = null,
)

data class DTOBaseFileInformation(
    val key:String? = null,
    var path:MutableList<String>? = null
)