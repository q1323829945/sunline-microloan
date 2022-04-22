package cn.sunline.saas.risk.control.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.risk.control.modules.db.RiskControlRule

interface RiskControlRuleRepository: BaseRepository<RiskControlRule,Long> {
}