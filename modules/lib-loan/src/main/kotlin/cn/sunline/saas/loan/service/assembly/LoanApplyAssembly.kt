package cn.sunline.saas.loan.service.assembly

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SuccessRequestException
import cn.sunline.saas.loan.exception.SubTypeIsIrrelevantParentTypeException
import cn.sunline.saas.loan.model.dto.*
import cn.sunline.saas.loan.model.enum.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal

object LoanApplyAssembly {
    private val objectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    fun convertToNewClientLoan(dtoLoanAgent: DTOLoanAgent,questionnaires:List<DTOQuestionnaire>? = null): DTONewClientLoan {
        return DTONewClientLoan(
            applicationId = dtoLoanAgent.applicationId!!,
            customerInformation = DTOCustomerInformation(
                name = dtoLoanAgent.personalInformation?.name,
                birthDay = dtoLoanAgent.personalInformation?.birthDay,
            ),
            personalInformation = ClientPersonalInformation(
                gender = dtoLoanAgent.personalInformation?.gender,
                civilStatus = dtoLoanAgent.personalInformation?.civilStatus,
                citizenship = dtoLoanAgent.personalInformation?.citizenship,
                tin = dtoLoanAgent.personalInformation?.tin,
            ),
            contactInformation = ClientContactInformation(
                mobileInformation = dtoLoanAgent.contactInformation?.mobile,
                personalEmail = dtoLoanAgent.contactInformation?.email,
                homeLandline = dtoLoanAgent.contactInformation?.homeLandLine,
                homeAddress = dtoLoanAgent.contactInformation?.address,
                workEmail = dtoLoanAgent.employeeInformation?.email,
                workAddress = dtoLoanAgent.employeeInformation?.address,
                workLandline = dtoLoanAgent.employeeInformation?.homeLandLine
            ),
            productInformation = DTOProductInformation(
                details = DTOProductDetails(
                    amount = dtoLoanAgent.loanInformation?.amount?.run { BigDecimal(this) },
                    term = dtoLoanAgent.loanInformation?.term,
                    purpose = dtoLoanAgent.loanInformation?.purpose
                )
            ),
            channel = dtoLoanAgent.channel,
            dataPrivacyConsent = DTOSignature(
                dtoLoanAgent.signature
            ),
            consentForTheIssuance = DTOSignature(
                dtoLoanAgent.signature
            ),
            customerUndertaking = DTOSignature(
                dtoLoanAgent.signature
            ),
            signature = dtoLoanAgent.signature,
            questionnaires = questionnaires
        )
    }

    fun convertToNewClientLoan(data: String): DTONewClientLoan {
        val loan: DTONewClientLoan
        try {
            loan = objectMapper.readValue(data)
        } catch (e:Exception){
            throw BusinessException("Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER,null,e.message)
        }
        return loan
    }

    fun convertToNewClientLoanView(data: String): DTONewClientLoanView {
        return objectMapper.convertValue(convertToNewClientLoan(data))
    }

    fun convertToClientLoan(dtoLoanAgent: DTOLoanAgent,questionnaires:List<DTOQuestionnaire>? = null): DTOClientLoan {
        return DTOClientLoan(
            applicationId = dtoLoanAgent.applicationId!!,
            customerInformation = DTOCustomerInformation(
                name = dtoLoanAgent.personalInformation?.name,
                birthDay = dtoLoanAgent.personalInformation?.birthDay,
            ),
            productInformation = DTOProductInformation(
                details = DTOProductDetails(
                    amount = dtoLoanAgent.loanInformation?.amount?.run { BigDecimal(this) },
                    term = dtoLoanAgent.loanInformation?.term,
                    purpose = dtoLoanAgent.loanInformation?.purpose
                )
            ),
            channel = dtoLoanAgent.channel,
            questionnaires = questionnaires
        )
    }

    fun convertToClientLoan(data: String): DTOClientLoan {
        val loan: DTOClientLoan
        try {
            loan = objectMapper.readValue(data)
        } catch (e:Exception){
            throw BusinessException("Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER,null,e.message)
        }
        return loan
    }

    fun convertToClientLoanView(data: String): DTOClientLoanView {
        return objectMapper.convertValue(convertToClientLoan(data))
    }

    fun convertToTeacherLoan(dtoLoanAgent: DTOLoanAgent): DTOTeacherLoan {
        return DTOTeacherLoan(
            applicationId = dtoLoanAgent.applicationId!!,
            personalInformation = DTOTeacherPersonalInformation(
                name = dtoLoanAgent.personalInformation?.name,
                gender = dtoLoanAgent.personalInformation?.gender,
                birthDay = dtoLoanAgent.personalInformation?.birthDay,
                citizenship = dtoLoanAgent.personalInformation?.citizenship,
                numberOfDependents = dtoLoanAgent.personalInformation?.numberOfDependents ?: 0,
                age = null, //TODO: from birthDay
            ),
            contactInformation = DTOContactInformation(
                mobile = dtoLoanAgent.contactInformation?.mobile?.number,
                email = dtoLoanAgent.contactInformation?.email,
                sss = dtoLoanAgent.personalInformation?.sss,
                tin = dtoLoanAgent.personalInformation?.tin,
                presentAddress = dtoLoanAgent.contactInformation?.address,
            ),
            educationalBackground = dtoLoanAgent.personalInformation?.educationalBackground,
            employmentInformation = DTOEmploymentInformation(
                employer = dtoLoanAgent.employeeInformation?.name,
                address = dtoLoanAgent.employeeInformation?.address,
                email = dtoLoanAgent.employeeInformation?.email
            ),
            spouseInformation = DTOTeacherSpouseInformation(
                name = dtoLoanAgent.personalInformation?.spouseInformation?.name,
            ),
            loanInformation = DTOLoanInformation(
                amount = dtoLoanAgent.loanInformation?.amount?.run { BigDecimal(this) },
                term = dtoLoanAgent.loanInformation?.term,
                purpose = dtoLoanAgent.loanInformation?.purpose?.run { mutableListOf(this) }
            ),
            channel = dtoLoanAgent.channel,
            signature = dtoLoanAgent.signature
        )
    }

    fun convertToTeacherLoan(data: String): DTOTeacherLoan {
        val loan: DTOTeacherLoan
        try {
            loan = objectMapper.readValue(data)
        } catch (e:Exception){
            throw BusinessException("Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER,null,e.message)
        }
        return loan
    }

    fun convertToTeacherLoanView(data: String): DTOTeacherLoanView {
        return objectMapper.convertValue(convertToTeacherLoan(data))
    }

    fun convertToKabuhayanLoan(dtoLoanAgent: DTOLoanAgent,questionnaires:List<DTOQuestionnaire>? = null): DTOKabuhayanLoan {
        return DTOKabuhayanLoan(
            applicationId = dtoLoanAgent.applicationId!!,
            personalInformation = DTOPersonalInformation(
                name = dtoLoanAgent.personalInformation?.name,
                mobile = dtoLoanAgent.contactInformation?.mobile?.number,
                email = dtoLoanAgent.contactInformation?.email,
            ),
            companyInformation = DTOCompanyInformation(
                name = dtoLoanAgent.employeeInformation?.name,
                address = dtoLoanAgent.employeeInformation?.address
            ),
            loanInformation = DTOLoanInformation(
                amount = dtoLoanAgent.loanInformation?.amount?.run { BigDecimal(this) },
                term = dtoLoanAgent.loanInformation?.term,
                purpose = dtoLoanAgent.loanInformation?.purpose?.run { mutableListOf(this) }
            ),
            channel = dtoLoanAgent.channel,
            signature = dtoLoanAgent.signature,
            questionnaires = questionnaires
        )
    }

    fun convertToKabuhayanLoan(data: String): DTOKabuhayanLoan {
        val loan: DTOKabuhayanLoan
        try {
            loan = objectMapper.readValue(data)
        } catch (e:Exception){
            throw BusinessException("Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER,null,e.message)
        }
        return loan
    }

    fun convertToKabuhayanLoanView(data: String): DTOKabuhayanLoanView {
        return objectMapper.convertValue(convertToKabuhayanLoan(data))
    }

    fun convertToCorporateLoan(dtoLoanAgent: DTOLoanAgent): DTOCorporateLoan {
        return DTOCorporateLoan(
            applicationId = dtoLoanAgent.applicationId!!,
            borrowerInformation = DTOBorrowerInformation(
                name = dtoLoanAgent.corporateInformation?.name,
                nature = dtoLoanAgent.corporateInformation?.nature,
                businessType = dtoLoanAgent.corporateInformation?.businessType,
                tin = dtoLoanAgent.corporateInformation?.tin,
                sss = dtoLoanAgent.corporateInformation?.sss,
                registration = dtoLoanAgent.corporateInformation?.registrationNo,
                businessAddress = dtoLoanAgent.corporateInformation?.address
            ),
            mortgagorInformation = DTOMortgagorInformation(
                name = dtoLoanAgent.personalInformation?.name,
                birthDay = dtoLoanAgent.personalInformation?.birthDay,
                gender = dtoLoanAgent.personalInformation?.gender,
                citizenship = dtoLoanAgent.personalInformation?.citizenship,
                civilStatus = dtoLoanAgent.personalInformation?.civilStatus,
                tin = dtoLoanAgent.personalInformation?.tin,
                sss = dtoLoanAgent.personalInformation?.sss,
                permanentAddress = dtoLoanAgent.contactInformation?.address,
                residencePhone = dtoLoanAgent.contactInformation?.homeLandLine,
                mobile = dtoLoanAgent.contactInformation?.mobile?.number,
            ),
            spouseInformation = DTOSpouseInformation(
                name = dtoLoanAgent.personalInformation?.spouseInformation?.name
            ),
            loanInformation = DTOCorporateLoanInformation(
                amount = dtoLoanAgent.loanInformation?.amount?.run { BigDecimal(this) },
                term = dtoLoanAgent.loanInformation?.term,
            ),
            channel = dtoLoanAgent.channel,
            undertaking = DTOUndertaking(
                signature = dtoLoanAgent.signature
            )
        )
    }

    fun convertToLoanAgent(data:String):DTOLoanAgent{
        val dtoLoanAgent: DTOLoanAgent
        try {
            dtoLoanAgent = objectMapper.readValue(data)
        } catch (e: Exception) {
            throw BusinessException(
                "Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER, null, e.message
            )
        }
        return dtoLoanAgent
    }

    fun convertToCorporateLoan(data: String): DTOCorporateLoan {
        val loan: DTOCorporateLoan
        try {
            loan = objectMapper.readValue(data)
        } catch (e:Exception){
            throw BusinessException("Incomplete parameters, please check spec",
                ManagementExceptionCode.REQUEST_INCOMPLETE_PARAMETER,null,e.message)
        }
        return loan
    }

    fun convertToCorporateLoanView(data: String): DTOCorporateLoanView {
        return objectMapper.convertValue(convertToCorporateLoan(data))
    }

    fun enumTypeCheck(employmentType: EmploymentType?, employmentSubType: EmploymentSubType?){
        if(employmentSubType != null && !employmentSubType.employmentTypes.contains(employmentType)){
            throw SubTypeIsIrrelevantParentTypeException("employmentSubType is irrelevant employmentType")
        }
    }

    fun enumTypeCheck(creditCardType: CreditCardType?, creditCardSubType: CreditCardSubType?){
        if(creditCardSubType != null && !creditCardSubType.creditCardTypes.contains(creditCardType)){
            throw SubTypeIsIrrelevantParentTypeException("creditCardSubType is irrelevant creditCardType")
        }
    }

    fun enumTypeCheck(positionType: PositionType?, positionSubType: PositionSubType?){
        if(positionSubType != null && !positionSubType.positionTypes.contains(positionType)){
            throw SubTypeIsIrrelevantParentTypeException("positionSubType is irrelevant positionType")
        }
    }
}