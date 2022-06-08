package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.CustomerStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatisticsFindParams
import cn.sunline.saas.statistics.repositories.CustomerStatisticsRepository
import javax.persistence.criteria.Predicate

@Service
class CustomerStatisticsService (
    private val customerStatisticsRepository: CustomerStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime):
    BaseMultiTenantRepoService<CustomerStatistics, Long>(customerStatisticsRepository) {

    fun findByDate(dtoCustomerStatisticsFindParams: DTOCustomerStatisticsFindParams): CustomerStatistics?{
        val lastDate = dtoCustomerStatisticsFindParams.datetime.plusDays(-1)
        return get { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),lastDate.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),lastDate.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),lastDate.dayOfMonth.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"),dtoCustomerStatisticsFindParams.frequency))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), ContextUtil.getTenant().toLong()))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


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