package cn.sunline.saas.channel.interest.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.channel.interest.model.InterestRate
import cn.sunline.saas.channel.interest.repository.InterestRateRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate

@Service
class InterestRateService (private val interestRateRepository: InterestRateRepository) :
    BaseMultiTenantRepoService<InterestRate, Long>(interestRateRepository) {

    @Autowired
    private lateinit var snowflakeService: Sequence

    fun addOne(interestRate: InterestRate):InterestRate{
        interestRate.id = snowflakeService.nextId()
        return save(interestRate)
    }

    fun updateOne(oldInterestRate: InterestRate, newInterestRate: InterestRate): InterestRate {
        oldInterestRate.period = newInterestRate.period
        oldInterestRate.rate = newInterestRate.rate
        return save(oldInterestRate)
    }

    fun deleteById(id:Long){
        interestRateRepository.deleteById(id)
    }

    fun findByRatePlanIdAndPeriod(
        ratePlanId: Long,
        period: LoanTermType
    ): InterestRate? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            period?.let { predicates.add(criteriaBuilder.equal(root.get<LoanTermType>("period"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun findByRatePlanId(ratePlanId: Long): List<InterestRate> {
        return this.getPage(ratePlanId, Pageable.unpaged()).content
    }

    fun getPage(ratePlanId: Long?, pageable: Pageable): Page<InterestRate> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            ratePlanId?.let { predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }
}