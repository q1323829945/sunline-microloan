package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.db.ApiStatistics
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.repositories.ApiStatisticsRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

@Service
class ApiStatisticsService (private val apiStatisticsRepository: ApiStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime):
    BaseMultiTenantRepoService<ApiStatistics, Long>(apiStatisticsRepository) {

    fun saveApiStatistics(dtoApiStatistics: DTOApiStatistics){
        val lastDateTime = tenantDateTime.now().plusDays(-1)

        save(
            ApiStatistics(
                id = sequence.nextId(),
                api = dtoApiStatistics.api,
                frequency = dtoApiStatistics.frequency,
                count = dtoApiStatistics.count,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate()
        ))
    }
}