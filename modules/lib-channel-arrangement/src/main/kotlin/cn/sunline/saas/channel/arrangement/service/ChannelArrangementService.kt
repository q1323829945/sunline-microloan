package cn.sunline.saas.channel.arrangement.service

import cn.sunline.saas.channel.arrangement.exception.ChannelArrangementNotFoundException
import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.model.dto.RangeValue
import cn.sunline.saas.channel.arrangement.repository.ChannelArrangementRepository
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import javax.persistence.criteria.Predicate


@Service
class ChannelArrangementService(private val channelArrangementRepo: ChannelArrangementRepository) :
    BaseMultiTenantRepoService<ChannelArrangement, Long>(channelArrangementRepo) {

    @Autowired
    private lateinit var seq: Sequence


    fun getPageByChannelAgreementId(channelAgreementId: Long, pageable: Pageable): Page<ChannelArrangement> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelAgreementId"), channelAgreementId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun getRangeValuesByChannelAgreementId(channelAgreementId: Long, pageable: Pageable): List<RangeValue> {
        val pages = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelAgreementId"), channelAgreementId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable).content
        val rangeValues = mutableListOf<RangeValue>()
        pages.forEach {
            rangeValues += RangeValue(
                commissionMethodType = it.commissionMethodType,
                lowerLimit = it.lowerLimit,
                upperLimit = it.upperLimit,
                rangeValue = (it.commissionRatio ?: it.commissionAmount) ?:
                throw ChannelArrangementNotFoundException("channel commission not found",ManagementExceptionCode.CHANNEL_COMMISSION_NOT_FOUND)
            )
        }
        return rangeValues
    }
}
