package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.TransactionType
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.BusinessStatistics
import cn.sunline.saas.statistics.modules.dto.DTOBusinessStatistics
import cn.sunline.saas.statistics.modules.dto.DTOBusinessStatisticsFindParams
import cn.sunline.saas.statistics.repositories.BusinessStatisticsRepository
import javax.persistence.criteria.Predicate

@Service
class BusinessStatisticsService (
    private val businessStatisticsRepository: BusinessStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime)
    :BaseMultiTenantRepoService<BusinessStatistics, Long>(businessStatisticsRepository) {


    fun findByDate(dtoBusinessStatisticsFindParams: DTOBusinessStatisticsFindParams): BusinessStatistics?{
        val lastDate = dtoBusinessStatisticsFindParams.datetime.plusDays(-1)
        return get { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("customerId"),dtoBusinessStatisticsFindParams.customerId))
            predicates.add(criteriaBuilder.equal(root.get<CurrencyType>("currency"),dtoBusinessStatisticsFindParams.currencyType))
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),lastDate.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),lastDate.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),lastDate.dayOfMonth.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"),dtoBusinessStatisticsFindParams.frequency))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), ContextUtil.getTenant().toLong()))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    fun saveBusinessStatistics(dtoBusinessStatistics:DTOBusinessStatistics){
        val lastDateTime = tenantDateTime.now().plusDays(-1)
        save(
            BusinessStatistics(
                id = sequence.nextId(),
                customerId = dtoBusinessStatistics.customerId,
                frequency = dtoBusinessStatistics.frequency,
                paymentAmount = dtoBusinessStatistics.paymentAmount,
                repaymentAmount = dtoBusinessStatistics.repaymentAmount,
                currency = dtoBusinessStatistics.currencyType,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate()
            )
        )
    }
}