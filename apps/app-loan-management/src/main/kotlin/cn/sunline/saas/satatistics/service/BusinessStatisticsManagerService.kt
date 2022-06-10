package cn.sunline.saas.satatistics.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOBusinessStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOCustomerStatisticsCount
import cn.sunline.saas.statistics.services.ApiStatisticsService
import cn.sunline.saas.statistics.services.BusinessStatisticsService
import cn.sunline.saas.statistics.services.CustomerStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class BusinessStatisticsManagerService {
    @Autowired
    private lateinit var businessStatisticsService: BusinessStatisticsService


    fun getStatisticsByDate(year:Long,month:Long,day:Long,tenantId:Long): List<DTOBusinessStatisticsCount> {
        val page = businessStatisticsService.getPaged({
            root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),year))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),month))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),day))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), Frequency.D))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), tenantId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())


        return page.content.groupBy { it.currency }.map { groupBy ->
            DTOBusinessStatisticsCount(
                groupBy.value.sumOf { it.paymentAmount }
                ,groupBy.value.sumOf { it.repaymentAmount }
                ,groupBy.key
                ,tenantId)
        }
    }

}