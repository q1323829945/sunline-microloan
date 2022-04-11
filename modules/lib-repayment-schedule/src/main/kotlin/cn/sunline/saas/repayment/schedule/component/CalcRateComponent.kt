

package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.criteria.Predicate

object CalcRateComponent {

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    @Autowired
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var interestFeatureService: InterestFeatureService

    fun calcBaseRate(loanRate: BigDecimal,loanRateType : LoanRateType, baseYearDays: BaseYearDays?): BigDecimal {
        return when (loanRateType) {
            LoanRateType.DAILY -> InterestRateUtil.toDayRate(baseYearDays!!,loanRate).setScale(6,RoundingMode.HALF_UP)
            LoanRateType.MONTHLY -> InterestRateUtil.toMonthRate(loanRate).setScale(6,RoundingMode.HALF_UP)
        }
    }


    fun getInterestRate(productId: String,term: Int): BigDecimal{
        val interestFeature = interestFeatureService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()
        val ratePlanId = interestFeature?.ratePlanId
        val ratePlan = ratePlanService.getOne(ratePlanId!!);
        val loanTermType = LoanTermType.valueOf(term.toString())
        val interestRates = ratePlan?.rates!!
        // RatePlanType.STANDARD TODO CUSTOMER
        return when (interestFeature.interestType) {
            InterestType.FIXED -> {
                InterestRateUtil.getRate(loanTermType,interestRates)
            }
            InterestType.FLOATING_RATE_NOTE -> {
                InterestRateUtil.getRate(loanTermType,interestRates)
            }
        }
    }
}