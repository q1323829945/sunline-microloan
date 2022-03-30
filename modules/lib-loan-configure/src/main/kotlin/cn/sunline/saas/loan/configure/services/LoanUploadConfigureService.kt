package cn.sunline.saas.loan.configure.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.repositories.LoanUploadConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

@Service
class LoanUploadConfigureService  (private var baseRepository: LoanUploadConfigureRepository) : BaseRepoService<LoanUploadConfigure, Long>(baseRepository) {
    @Autowired
    private lateinit var sequence: Sequence

    fun addOne(loanUploadConfigure: LoanUploadConfigure): LoanUploadConfigure {
        loanUploadConfigure.id = sequence.nextId()
        return this.save(loanUploadConfigure)
    }

    fun findAllExist():List<LoanUploadConfigure>{
        return baseRepository.findByDeleted(false)
    }
}