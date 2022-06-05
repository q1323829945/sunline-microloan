package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.statistics.modules.db.ApiStatistics
import cn.sunline.saas.statistics.repositories.ApiStatisticsRepository
import org.springframework.stereotype.Service

@Service
class ApiStatisticsService (private val apiStatisticsRepository: ApiStatisticsRepository):
    BaseMultiTenantRepoService<ApiStatistics, Long>(apiStatisticsRepository) {

}