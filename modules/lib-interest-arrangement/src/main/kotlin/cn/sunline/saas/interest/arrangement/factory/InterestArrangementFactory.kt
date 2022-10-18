package cn.sunline.saas.interest.arrangement.factory

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.model.InterestRate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.math.BigDecimal

/**
 * @title: InterestArrangementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:13
 */
@Component
class InterestArrangementFactory {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun instance(
        agreementId: Long,
        amount: BigDecimal,
        term: LoanTermType,
        dtoInterestArrangementAdd: DTOInterestArrangementAdd,
    ): InterestArrangement {
        val planRates = objectMapper.convertValue<MutableList<InterestRate>>(dtoInterestArrangementAdd.planRates)
        return InterestArrangement(
            id = agreementId,
            interestType = dtoInterestArrangementAdd.interestType,
            rate = InterestRateHelper.getTermOrAmountRate(amount, term, planRates)!!,
            baseYearDays = dtoInterestArrangementAdd.baseYearDays,
            adjustFrequency = dtoInterestArrangementAdd.adjustFrequency,
            overdueInterestRatePercentage = BigDecimal(dtoInterestArrangementAdd.overdueInterestRatePercentage),
            baseRate = dtoInterestArrangementAdd.baseRate?.run { BigDecimal(this) },
            basicPoint = dtoInterestArrangementAdd.basicPoint,
            floatRatio = dtoInterestArrangementAdd.floatRatio,
        )
    }


}