package cn.sunline.saas.channel.arrangement.service

import cn.sunline.saas.channel.arrangement.exception.ChannelArrangementNotFoundException
import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.db.ChannelCommissionItems
import cn.sunline.saas.channel.arrangement.model.dto.RangeValue
import cn.sunline.saas.channel.arrangement.repository.ChannelCommissionItemsRepository
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@Service
class ChannelCommissionItemsService(private val channelCommissionItemsRepo: ChannelCommissionItemsRepository) :
    BaseMultiTenantRepoService<ChannelCommissionItems, Long>(channelCommissionItemsRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun getPageByChannelArrangementId(
        channelArrangementId: Long,
        applyStatus: ApplyStatus?,
        pageable: Pageable
    ): Page<ChannelCommissionItems> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelArrangementId"), channelArrangementId))
            applyStatus?.let { predicates.add(criteriaBuilder.equal(root.get<ApplyStatus>("applyStatus"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

}