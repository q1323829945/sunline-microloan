package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.CustomerStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatistics
import cn.sunline.saas.statistics.repositories.CustomerStatisticsRepository

@Service
class CustomerStatisticsService (
    private val customerStatisticsRepository: CustomerStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime):
    BaseMultiTenantRepoService<CustomerStatistics, Long>(customerStatisticsRepository) {

    fun saveCustomerStatistics(dtoCustomerStatistics: DTOCustomerStatistics){
        val lastDateTime = tenantDateTime.now().plusDays(-1)

        save(CustomerStatistics(
            id = sequence.nextId(),
            frequency = dtoCustomerStatistics.frequency,
            personCount = dtoCustomerStatistics.personCount,
            organisationCount = dtoCustomerStatistics.organisationCount,
            partyCount = dtoCustomerStatistics.partyCount,
            year = lastDateTime.year.toLong(),
            month = lastDateTime.monthOfYear.toLong(),
            day = lastDateTime.dayOfMonth.toLong(),
            datetime = tenantDateTime.now().toDate(),
        ))
    }
}