package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.repository.InterestRateRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
}