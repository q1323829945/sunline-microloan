package cn.sunline.saas.loan.service

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.loan.exception.LoanAgentHasBeenProductTypeException
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.exception.LoanApplyStatusException
import cn.sunline.saas.loan.model.db.LoanAgent
import cn.sunline.saas.loan.model.dto.DTOLoanAgent
import cn.sunline.saas.loan.model.dto.DTOLoanApplyStatus
import cn.sunline.saas.loan.model.enum.LoanType.CORPORATE
import cn.sunline.saas.loan.model.enum.LoanType.INDIVIDUAL
import cn.sunline.saas.loan.repository.LoanAgentRepository
import cn.sunline.saas.loan.service.assembly.LoanApplyAssembly
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoanAgentService (
    private var loanAgentRepos: LoanAgentRepository,
    private var sequence: Sequence
) : BaseMultiTenantRepoService<LoanAgent, Long>(loanAgentRepos) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(data:String):LoanAgent{
        val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(data)
        return save(
            LoanAgent(
                applicationId = sequence.nextId(),
                seq = dtoLoanAgent.seq,
                name = getName(dtoLoanAgent),
                data = data,
                channelCode = dtoLoanAgent.channel.code,
                channelName = dtoLoanAgent.channel.name
            )
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateOne(applicationId:Long,productId:Long):LoanAgent{
        val loanAgent = getOne(applicationId)?: throw LoanApplyNotFoundException("Invalid loan apply")

        loanAgent.productId?.run {
            throw LoanAgentHasBeenProductTypeException("The loan apply has been product!")
        }
        loanAgent.productId = productId
        return save(loanAgent)
    }

    fun updateStatus(applicationId: Long,status: ApplyStatus): LoanAgent {
        val loanApply = getOne(applicationId)?: throw LoanApplyNotFoundException("Invalid loan apply")
        if(status == ApplyStatus.PROCESSING && loanApply.status != ApplyStatus.RECORD ){
            throw LoanApplyStatusException("The loan Apply already submit ,please wait!")
        }
        loanApply.status = status

        return save(loanApply)
    }

    fun getStatus(applicationId:Long): DTOLoanApplyStatus {
        val loanApply = getOne(applicationId)?: throw LoanApplyNotFoundException("Invalid loan apply")
        return objectMapper.convertValue(loanApply)
    }

    private fun getName(dtoLoanAgent: DTOLoanAgent):String{
        val name = dtoLoanAgent.loanType?.run {
            when(this){
                INDIVIDUAL -> {
                    dtoLoanAgent.personalInformation?.name?.run {
                        var name = "${firstName}·"
                        if(!middleName.isNullOrEmpty()){
                            name += "${this.middleName}·"
                        }
                        name += lastName
                        if(!suffix.isNullOrEmpty()){
                            name += " ${this.suffix}"
                        }
                        return name
                    }
                }
                CORPORATE -> dtoLoanAgent.corporateInformation?.name
            }
        }

        return name?:""
    }

    fun getDetails(applicationId: Long):DTOLoanAgent{
        val loanAgent = getOne(applicationId)?:throw LoanApplyNotFoundException("Invalid loan apply")
        val data = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)
        data.applicationId = loanAgent.applicationId.toString()
        data.productType = loanAgent.loanApply?.productType
        data.productId = loanAgent.productId?.toString()
        return data
    }
}