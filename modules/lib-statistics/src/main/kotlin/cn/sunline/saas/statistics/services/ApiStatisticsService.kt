package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.db.ApiStatistics
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.repositories.ApiStatisticsRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.dto.DTOApiStatisticsFindParams
import javax.persistence.criteria.Predicate

@Service
class ApiStatisticsService (private val apiStatisticsRepository: ApiStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime):
    BaseMultiTenantRepoService<ApiStatistics, Long>(apiStatisticsRepository) {

    fun findByDate(dtoApiStatisticsFindParams: DTOApiStatisticsFindParams):ApiStatistics?{
        val lastDate = dtoApiStatisticsFindParams.datetime.plusDays(-1)
        return get { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),lastDate.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),lastDate.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),lastDate.dayOfMonth.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"),dtoApiStatisticsFindParams.frequency))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"),ContextUtil.getTenant().toLong()))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

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