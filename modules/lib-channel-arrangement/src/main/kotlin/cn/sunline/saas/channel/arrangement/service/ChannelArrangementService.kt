package cn.sunline.saas.channel.arrangement.service

import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.repository.ChannelArrangementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
class ChannelArrangementService(private val channelArrangementRepo: ChannelArrangementRepository) :
    BaseMultiTenantRepoService<ChannelArrangement, Long>(channelArrangementRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(
        channelAgreementId: Long, dtoChannelArrangementAdd: DTOChannelArrangementAdd
    ): ChannelArrangement {
        return save(
            ChannelArrangement(
                id = seq.nextId(),
                channelAgreementId = channelAgreementId,
                commissionType = dtoChannelArrangementAdd.commissionType,
                commissionMethodType = dtoChannelArrangementAdd.commissionMethodType,
                commissionAmount = dtoChannelArrangementAdd.commissionAmount,
                commissionRatio = dtoChannelArrangementAdd.commissionRatio
            )
        )
    }

    fun getOneByChannelId(channelAgreementId: Long): ChannelArrangement {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelAgreementId"), channelAgreementId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }!!//?: ChannelArrangementNotFoundException("Invalid Channel Arrangement")
    }

}

