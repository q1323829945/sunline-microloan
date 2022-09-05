package cn.sunline.saas.services

import cn.sunline.saas.modules.db.LoanApplyAudit
import cn.sunline.saas.modules.dto.DTOLoanApplyAuditAdd
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repositories.LoanApplyAuditRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
@Service
class LoanApplyAuditService (
    private val loanApplyRepository: LoanApplyAuditRepository,
    private val sequence: Sequence) :
    BaseMultiTenantRepoService<LoanApplyAudit, String>(loanApplyRepository) {


    fun addLoanApplyAudit(dtoLoanApplyAuditAdd: DTOLoanApplyAuditAdd){
        save(
            LoanApplyAudit(
                id = sequence.nextId(),
                applicationId = dtoLoanApplyAuditAdd.applicationId,
                name = dtoLoanApplyAuditAdd.name,
                productId = dtoLoanApplyAuditAdd.productId,
                term = dtoLoanApplyAuditAdd.term,
                amount = dtoLoanApplyAuditAdd.amount,
                data = dtoLoanApplyAuditAdd.data,
                status = dtoLoanApplyAuditAdd.status
            )
        )
    }


}