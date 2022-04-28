package cn.sunline.saas.risk.control.rule.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.rule.repositories.RiskControlRuleParamRepository
import org.springframework.stereotype.Service

@Service
class RiskControlRuleParamService(private val riskControlRuleParamRepository: RiskControlRuleParamRepository):
    BaseMultiTenantRepoService<RiskControlRuleParam, Long>(riskControlRuleParamRepository) {

    fun deleteByRuleId(ruleId:Long?){
        riskControlRuleParamRepository.deleteByRuleId(ruleId)
    }

}