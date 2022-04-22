package cn.sunline.saas.risk.control.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.risk.control.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.repositories.RiskControlRuleParamRepository
import org.springframework.stereotype.Service

@Service
class RiskControlRuleParamService(private val riskControlRuleParamRepository: RiskControlRuleParamRepository):
    BaseMultiTenantRepoService<RiskControlRuleParam, Long>(riskControlRuleParamRepository) {

    fun updateAll(ruleId: Long, params:List<RiskControlRuleParam>){
        val list = riskControlRuleParamRepository.findByRuleId(ruleId)

    }

    fun deleteByRuleId(ruleId:Long?){
        riskControlRuleParamRepository.deleteByRuleId(ruleId)
    }

}