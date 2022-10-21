package cn.sunline.saas.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.modules.db.Statistics
import cn.sunline.saas.modules.dto.DTOStatistics
import cn.sunline.saas.repository.StatisticsRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

@Service
class StatisticsService (
    private val statisticsRepository: StatisticsRepository,
    private val sequence: Sequence
) : BaseRepoService<Statistics, Long>(statisticsRepository){

    fun addOne(dtoStatistics: DTOStatistics):Statistics{
        return save(
            Statistics(
                id = sequence.nextId(),
                tenant = dtoStatistics.tenant,
                server = dtoStatistics.server,
                method = dtoStatistics.method,
                path = dtoStatistics.path,
                query = dtoStatistics.query,
                datetime = DateTime.now().toDate()
            )
        )
    }

    fun findCountByDate(tenant:String,year:Int,month:Int,day:Int):Int{
        val startDay = DateTime().withDate(year,month,day).withTime(0,0,0,0)
        val endDay = startDay.plusDays(1)
        return getPaged({ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),startDay.toDate(),endDay.toDate()))
            predicates.add(criteriaBuilder.equal(root.get<String>("tenant"),tenant))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).count()
    }
}