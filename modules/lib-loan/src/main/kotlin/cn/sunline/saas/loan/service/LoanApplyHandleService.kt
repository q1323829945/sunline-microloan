package cn.sunline.saas.loan.service

import cn.sunline.saas.loan.model.db.LoanApplyHandle
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.repository.LoanApplyHandleRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.stereotype.Service

@Service
class LoanApplyHandleService  (
    private var loanApplyHandleRepos: LoanApplyHandleRepository,
    private var tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<LoanApplyHandle, Long>(loanApplyHandleRepos) {

    fun saveOne(dtoLoanApplyHandle: DTOLoanApplyHandle){
        val loanApplyHandle = getOne(dtoLoanApplyHandle.applicationId.toLong())
        loanApplyHandle?.run {
            updateOne(loanApplyHandle,dtoLoanApplyHandle)
        }?:run {
            addOne(dtoLoanApplyHandle)
        }
    }

    private fun addOne(dtoLoanApplyHandle: DTOLoanApplyHandle){
        save(
            LoanApplyHandle(
                applicationId = dtoLoanApplyHandle.applicationId.toLong(),
                supplement = dtoLoanApplyHandle.supplement!!

            )
        )
    }

    private fun updateOne(loanApplyHandle: LoanApplyHandle,dtoLoanApplyHandle: DTOLoanApplyHandle){
        dtoLoanApplyHandle.supplement?.run {
            loanApplyHandle.supplement = this
        }
        dtoLoanApplyHandle.supplementDate?.run {
            loanApplyHandle.supplementDate = this
        }
        save(loanApplyHandle)
    }
}