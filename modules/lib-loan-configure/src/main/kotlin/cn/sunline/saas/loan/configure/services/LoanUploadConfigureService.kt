package cn.sunline.saas.loan.configure.services

import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.loan.configure.repositories.LoanUploadConfigureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

@Service
class LoanUploadConfigureService  (private var baseRepository: LoanUploadConfigureRepository) : BaseMultiTenantRepoService<LoanUploadConfigure, Long>(baseRepository) {
    @Autowired
    private lateinit var sequence: Sequence

    fun addOne(loanUploadConfigure: LoanUploadConfigure): LoanUploadConfigure {
        loanUploadConfigure.id = sequence.nextId()
        return this.save(loanUploadConfigure)
    }

    fun findAllExist():List<LoanUploadConfigure>{

        val page = getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"),false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
        return page.toList()
    }
}