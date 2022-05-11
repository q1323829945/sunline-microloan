package cn.sunline.saas.document.template.services

import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure
import cn.sunline.saas.document.template.repositories.LoanUploadConfigureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

@Service
class LoanUploadConfigureService  (private var baseRepository: LoanUploadConfigureRepository) : BaseMultiTenantRepoService<LoanUploadConfigure, Long>(baseRepository) {
    @Autowired
    private lateinit var sequence: Sequence

    fun addOne(loanUploadConfigure: LoanUploadConfigure): LoanUploadConfigure {
        loanUploadConfigure.id = sequence.nextId()
        return this.save(loanUploadConfigure)
    }
}