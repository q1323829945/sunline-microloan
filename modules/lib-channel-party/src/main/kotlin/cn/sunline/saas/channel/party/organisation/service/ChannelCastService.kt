package cn.sunline.saas.channel.party.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.party.organisation.model.db.ChannelCast
import cn.sunline.saas.channel.party.organisation.repository.ChannelCastRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class ChannelCastService (
    private val channelCastRepos: ChannelCastRepository,
    private val tenantDateTime: TenantDateTime
) :
    BaseMultiTenantRepoService<ChannelCast, Long>(channelCastRepos) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getPaged(pageable: Pageable): Page<ChannelCast> {
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            query.orderBy(criteriaBuilder.desc(root.get<Date>("dateTime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun getChannelCast(channelCode: String, channelName: String): ChannelCast? {
        val channelCast = get { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("channelCode"), channelCode))
            //predicates.add(criteriaBuilder.equal(root.get<String>("channelName"), channelName))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
        return channelCast
    }

    fun getChannelCastPaged(channelCode: String?, channelName: String?, pageable: Pageable): Page<ChannelCast> {
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            channelCode?.run { predicates.add(criteriaBuilder.equal(root.get<String>("channelCode"), channelCode)) }
            channelName?.run { predicates.add(criteriaBuilder.equal(root.get<String>("channelName"), channelName)) }
            query.orderBy(criteriaBuilder.desc(root.get<Date>("dateTime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun getUniqueChannelCast(channelCode: String,channelName: String):ChannelCast?{
        return get{ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("channelCode"), channelCode))
            predicates.add(criteriaBuilder.equal(root.get<String>("channelName"), channelName))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}


