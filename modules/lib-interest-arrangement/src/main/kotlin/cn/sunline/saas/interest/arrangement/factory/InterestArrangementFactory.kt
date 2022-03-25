package cn.sunline.saas.interest.arrangement.factory

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @title: InterestArrangementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:13
 */
@Component
class InterestArrangementFactory {

    fun instance(agreementId:Long,term: LoanTermType, dtoInterestArrangementAdd: DTOInterestArrangementAdd): InterestArrangement {
        return InterestArrangement(
            id = agreementId,
            interestType = dtoInterestArrangementAdd.interestType,
            rate = InterestRateUtil.getRate(term, dtoInterestArrangementAdd.planRates),
            baseYearDays = dtoInterestArrangementAdd.baseYearDays,
            adjustFrequency = dtoInterestArrangementAdd.adjustFrequency,
            overdueInterestRatePercentage = dtoInterestArrangementAdd.overdueInterestRatePercentage
        )
    }

}