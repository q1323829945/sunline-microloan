package cn.sunline.saas.risk.control.rule.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam

interface RiskControlRuleParamRepository: BaseRepository<RiskControlRuleParam, Long> {
    fun deleteByRuleId(ruleId:Long?)
}