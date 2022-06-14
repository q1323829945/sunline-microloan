package cn.sunline.saas.repayment.arrangement.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentAccount
import cn.sunline.saas.repayment.arrangement.repository.RepaymentAccountRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


@Service
class RepaymentAccountService (private val repaymentAccountRepos: RepaymentAccountRepository) :
    BaseMultiTenantRepoService<RepaymentAccount, Long>(repaymentAccountRepos){

    fun retrieveByRepaymentAccount(
        repaymentAccount: String
    ): Page<RepaymentAccount> {
        val specification: Specification<RepaymentAccount> =
            Specification { root: Root<RepaymentAccount>, _, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()
                repaymentAccount.run { predicates.add(criteriaBuilder.equal(root.get<Long>("repaymentAccount"), repaymentAccount)) }
                criteriaBuilder.and(*(predicates.toTypedArray()))
            }
        return getPageWithTenant(specification, Pageable.unpaged())
    }
}
