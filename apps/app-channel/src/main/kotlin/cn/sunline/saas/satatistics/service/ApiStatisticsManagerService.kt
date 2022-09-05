package cn.sunline.saas.satatistics.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import cn.sunline.saas.channel.statistics.services.ApiStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ApiStatisticsManagerService {
    @Autowired
    private lateinit var apiStatisticsService:ApiStatisticsService


    fun getStatisticsByDate(year:Long,month:Long,day:Long,tenantId:Long): DTOApiStatisticsCount {
        val count = apiStatisticsService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),year))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),month))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),day))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), Frequency.D))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), tenantId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).sumOf { it.count }

        return DTOApiStatisticsCount(count,tenantId)

    }

}