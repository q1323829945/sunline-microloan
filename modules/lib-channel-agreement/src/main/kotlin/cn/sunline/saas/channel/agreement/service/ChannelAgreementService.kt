package cn.sunline.saas.channel.agreement.service

import cn.sunline.saas.channel.agreement.exception.ChannelAgreementNotFoundException
import cn.sunline.saas.channel.agreement.factory.ChannelAgreementFactory
import cn.sunline.saas.channel.agreement.model.db.ChannelAgreement
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementPageView
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementView
import cn.sunline.saas.channel.agreement.repository.ChannelAgreementRepository
import cn.sunline.saas.channel.arrangement.exception.ChannelArrangementNotFoundException
import cn.sunline.saas.channel.arrangement.service.ChannelArrangementService
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate
import javax.transaction.Transactional


@Service
class ChannelAgreementService(
    private val channelAgreementRepo: ChannelAgreementRepository,
    private val tenantDateTime: TenantDateTime
) :
    BaseMultiTenantRepoService<ChannelAgreement, Long>(channelAgreementRepo) {
    @Autowired
    private lateinit var channelAgreementFactory: ChannelAgreementFactory

    @Autowired
    private lateinit var channelArrangementService: ChannelArrangementService

    @Transactional
    fun registered(dtoChannelAgreementAdd: DTOChannelAgreementAdd): DTOChannelAgreementView {
        val channelAgreement = save(channelAgreementFactory.instance(dtoChannelAgreementAdd))
        val channelArrangement =
            channelArrangementService.registered(channelAgreement.id, dtoChannelAgreementAdd.channelArrangement)

        return DTOChannelAgreementView(
            channelAgreement = channelAgreement,
            channelArrangement = channelArrangement
        )
    }

    fun getOneByChannelIdAndAgreementType(channelId: Long, agreementType: AgreementType): ChannelAgreement? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelId"), channelId))
            predicates.add(criteriaBuilder.equal(root.get<AgreementType>("agreementType"), agreementType))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun getDetail(agreementId: Long): DTOChannelAgreementView? {
        val channelAgreement = getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("id"), agreementId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }!!//?: ChannelAgreementNotFoundException("Invalid Channel agreement")
        val channelArrangement = channelArrangementService.getOneByChannelId(channelAgreement.id)
        return DTOChannelAgreementView(
            channelAgreement = channelAgreement,
            channelArrangement = channelArrangement
        )
    }

    fun getPageByChannelId(channelId: Long, pageable: Pageable): Page<DTOChannelAgreementPageView> {
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("channelId"), channelId))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("signedDate")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable).map {
            DTOChannelAgreementPageView(
                id = it.id.toString(),
                channelId = it.channelId.toString(),
                agreementType = it.agreementType,
                signedDate = tenantDateTime.toTenantDateTime(it.signedDate).toString(),
                fromDateTime = tenantDateTime.toTenantDateTime(it.fromDateTime).toString(),
                toDateTime = tenantDateTime.toTenantDateTime(it.toDateTime).toString()
            )
        }
    }


}

