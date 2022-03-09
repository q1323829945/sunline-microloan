package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.repository.InterestRateRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService

class InterestRateService (private val interestRateRepository: InterestRateRepository) :
        BaseMultiTenantRepoService<InterestRate, Long>(interestRateRepository) {

    fun updateOne(oldInterestRate: InterestRate, newInterestRate: InterestRate): InterestRate {
        oldInterestRate.period = newInterestRate.period
        oldInterestRate.rate = newInterestRate.rate
        return save(oldInterestRate)
    }
}