package cn.sunline.saas.loan.service

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.constant.ProductType.*
import cn.sunline.saas.loan.exception.ApplicationIdAlreadyExistException
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.exception.LoanApplyStatusException
import cn.sunline.saas.loan.exception.ProductTypeCannotBeChangeExistException
import cn.sunline.saas.loan.model.db.LoanApply
import cn.sunline.saas.loan.model.db.LoanApplyHandle
import cn.sunline.saas.loan.model.dto.*
import cn.sunline.saas.loan.repository.LoanApplyRepository
import cn.sunline.saas.loan.service.assembly.LoanApplyAssembly
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.criteria.Predicate

@Service
class LoanApplyService (
    private var loanApplyRepos: LoanApplyRepository,
    private val tenantDateTime: TenantDateTime
    ) :BaseMultiTenantRepoService<LoanApply, Long>(loanApplyRepos) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun addNewClientLoan(loan: DTONewClientLoan):LoanApply{
        return addOne(
            DTOLoanApplyAdd(
                applicationId = loan.applicationId,
                productType = NEW_CLIENT,
                term = loan.productInformation?.details?.term,
                amount = loan.productInformation?.details?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun addClientLoan(loan:DTOClientLoan):LoanApply{
        return addOne(
            DTOLoanApplyAdd(
                applicationId = loan.applicationId,
                productType = CLIENT,
                term = loan.productInformation?.details?.term,
                amount = loan.productInformation?.details?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun addCorporateLoan(loan:DTOCorporateLoan):LoanApply{
        return addOne(
            DTOLoanApplyAdd(
                applicationId = loan.applicationId,
                productType = CORPORATE,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun addKabuhayanLoan(loan:DTOKabuhayanLoan):LoanApply{
        return addOne(
            DTOLoanApplyAdd(
                applicationId = loan.applicationId,
                productType = KABUHAYAN,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun addTeacherLoan(loan:DTOTeacherLoan):LoanApply{
        return addOne(
            DTOLoanApplyAdd(
                applicationId = loan.applicationId,
                productType = TEACHER,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    private fun addOne(dtoLoanApplyAdd: DTOLoanApplyAdd): LoanApply {
        val check = getOne(dtoLoanApplyAdd.applicationId.toLong())
        check?.run {
            throw ApplicationIdAlreadyExistException("ApplicationId ${dtoLoanApplyAdd.applicationId} already exist!")
        }

        return save(
            LoanApply(
                applicationId = dtoLoanApplyAdd.applicationId.toLong(),
                productType = dtoLoanApplyAdd.productType,
                term = dtoLoanApplyAdd.term,
                amount = dtoLoanApplyAdd.amount,
                data = dtoLoanApplyAdd.data
            )
        )
    }

    fun getLoanApplyDetails(applicationId: Long):Any{
        val loanApply = getOne(applicationId)?: throw LoanApplyNotFoundException("Invalid loan apply")
        return when(loanApply.productType){
            NEW_CLIENT -> LoanApplyAssembly.convertToNewClientLoanView(loanApply.data)
            CLIENT -> LoanApplyAssembly.convertToClientLoanView(loanApply.data)
            TEACHER -> LoanApplyAssembly.convertToTeacherLoanView(loanApply.data)
            KABUHAYAN -> LoanApplyAssembly.convertToKabuhayanLoanView(loanApply.data)
            CORPORATE -> LoanApplyAssembly.convertToCorporateLoanView(loanApply.data)
        }
    }

    fun updateNewClientLoan(data: String):LoanApply{
        val loan = LoanApplyAssembly.convertToNewClientLoan(data)

        LoanApplyAssembly.enumTypeCheck(loan.financialInformation?.employmentType,loan.financialInformation?.employmentSubType)
        LoanApplyAssembly.enumTypeCheck(loan.financialInformation?.position?.positionType,loan.financialInformation?.position?.positionSubType)
        loan.productInformation?.creditCardDetails?.run {
            LoanApplyAssembly.enumTypeCheck(creditCardType,creditCardSubType)
        }
        return updateOne(
            DTOLoanApplyChange(
                applicationId = loan.applicationId,
                productType = NEW_CLIENT,
                term = loan.productInformation?.details?.term,
                amount = loan.productInformation?.details?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun updateClientLoan(data: String):LoanApply{
        val loan = LoanApplyAssembly.convertToClientLoan(data)

        LoanApplyAssembly.enumTypeCheck(loan.financialInformation?.employmentType,loan.financialInformation?.employmentSubType)
        loan.productInformation?.creditCardDetails?.run {
            LoanApplyAssembly.enumTypeCheck(creditCardType,creditCardSubType)
        }
        return updateOne(
            DTOLoanApplyChange(
                applicationId = loan.applicationId,
                productType = CLIENT,
                term = loan.productInformation?.details?.term,
                amount = loan.productInformation?.details?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun updateCorporateLoan(data: String):LoanApply{
        val loan = LoanApplyAssembly.convertToCorporateLoan(data)
        return updateOne(
            DTOLoanApplyChange(
                applicationId = loan.applicationId,
                productType = CORPORATE,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun updateKabuhayanLoan(data: String):LoanApply{
        val loan = LoanApplyAssembly.convertToKabuhayanLoan(data)
        return updateOne(
            DTOLoanApplyChange(
                applicationId = loan.applicationId,
                productType = KABUHAYAN,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    fun updateTeacherLoan(data: String):LoanApply{
        val loan = LoanApplyAssembly.convertToTeacherLoan(data)
        return updateOne(
            DTOLoanApplyChange(
                applicationId = loan.applicationId,
                productType = TEACHER,
                term = loan.loanInformation?.term,
                amount = loan.loanInformation?.amount,
                data = objectMapper.writeValueAsString(loan),
            )
        )
    }

    @Transactional
    private fun updateOne(dtoLoanApplyChange: DTOLoanApplyChange): LoanApply {
        val oldOne = getOne(dtoLoanApplyChange.applicationId.toLong()) ?: throw LoanApplyNotFoundException("Invalid loan apply")
        if (oldOne.productType != dtoLoanApplyChange.productType) {
            throw ProductTypeCannotBeChangeExistException("ProductType cannot be change!")
        }
        oldOne.term = dtoLoanApplyChange.term
        oldOne.amount = dtoLoanApplyChange.amount
        oldOne.data = dtoLoanApplyChange.data

        return save(oldOne)
    }
}