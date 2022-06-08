package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.BusinessStatistics
import cn.sunline.saas.statistics.modules.dto.DTOBusinessStatistics
import cn.sunline.saas.statistics.repositories.BusinessStatisticsRepository

@Service
class BusinessStatisticsService (
    private val businessStatisticsRepository: BusinessStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime)
    :BaseMultiTenantRepoService<BusinessStatistics, Long>(businessStatisticsRepository) {

    fun saveBusinessStatistics(dtoBusinessStatistics:DTOBusinessStatistics){
        val lastDateTime = tenantDateTime.now().plusDays(-1)
        save(
            BusinessStatistics(
                id = sequence.nextId(),
                customerId = dtoBusinessStatistics.customerId,
                frequency = dtoBusinessStatistics.frequency,
                totalAmount = dtoBusinessStatistics.amount,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate()
            )
        )
    }
}