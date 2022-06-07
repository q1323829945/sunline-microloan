package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.BusinessStatistics
import cn.sunline.saas.statistics.repositories.BusinessStatisticsRepository

@Service
class BusinessStatisticsService (private val businessStatisticsRepository: BusinessStatisticsRepository):
    BaseMultiTenantRepoService<BusinessStatistics, Long>(businessStatisticsRepository) {

}