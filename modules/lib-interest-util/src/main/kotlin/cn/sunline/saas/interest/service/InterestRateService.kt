package cn.sunline.saas.interest.service

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.repository.InterestRateRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class InterestRateService(private val interestRateRepository: InterestRateRepository) :
    BaseMultiTenantRepoService<InterestRate, Long>(interestRateRepository) {

    @Autowired
    private lateinit var snowflakeService: Sequence

    fun addOne(interestRate: InterestRate): InterestRate {
        interestRate.id = snowflakeService.nextId()
        return save(interestRate)
    }

    fun updateOne(oldInterestRate: InterestRate, newInterestRate: InterestRate): InterestRate {
        newInterestRate.fromPeriod?.let { oldInterestRate.fromPeriod = it }
        newInterestRate.toPeriod?.let { oldInterestRate.toPeriod = it }
        newInterestRate.fromAmountPeriod?.let { oldInterestRate.fromAmountPeriod = it }
        newInterestRate.toAmountPeriod?.let { oldInterestRate.toAmountPeriod = it }
        oldInterestRate.rate = newInterestRate.rate
        return save(oldInterestRate)
    }

    fun deleteById(id: Long) {
        interestRateRepository.deleteById(id)
    }

    fun findByRatePlanIdAndPeriod(
        ratePlanId: Long,
        fromPeriod: LoanTermType?,
        toPeriod: LoanTermType?,
        fromAmountPeriod: LoanAmountTierType?,
        toAmountPeriod: LoanAmountTierType?
    ): InterestRate? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            fromPeriod?.let { predicates.add(criteriaBuilder.equal(root.get<LoanTermType>("fromPeriod"), it)) }
            toPeriod?.let { predicates.add(criteriaBuilder.equal(root.get<LoanTermType>("toPeriod"), it)) }
            fromAmountPeriod?.let {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<LoanAmountTierType>("fromAmountPeriod"),
                        it
                    )
                )
            }
            toAmountPeriod?.let {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<LoanAmountTierType>("toAmountPeriod"),
                        it
                    )
                )
            }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun findByRatePlanId(ratePlanId: Long): List<InterestRate> {
        return this.getPage(ratePlanId,Pageable.unpaged()).content
    }

    fun getPage(ratePlanId: Long?, pageable: Pageable): Page<InterestRate> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            ratePlanId?.let { predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }
}