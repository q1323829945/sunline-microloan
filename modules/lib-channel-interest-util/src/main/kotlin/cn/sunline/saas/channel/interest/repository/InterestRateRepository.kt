package cn.sunline.saas.channel.interest.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.channel.interest.model.InterestRate

interface InterestRateRepository : BaseRepository<InterestRate, Long>{

    fun findByRatePlanIdAndPeriod(ratePlanId: Long,period: LoanTermType): InterestRate?

    fun findByRatePlanId(ratePlanId: Long):List<InterestRate>?
}