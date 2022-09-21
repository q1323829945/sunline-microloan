package cn.sunline.saas.channel.party.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.party.organisation.exception.OrganisationNotFoundException
import cn.sunline.saas.channel.party.organisation.model.db.ChannelCast
import cn.sunline.saas.channel.party.organisation.model.db.Organisation
import cn.sunline.saas.channel.party.organisation.model.db.OrganisationIdentification
import cn.sunline.saas.channel.party.organisation.model.dto.DTOChannelCastView
import cn.sunline.saas.channel.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.channel.party.organisation.model.dto.DTOOrganisationChange
import cn.sunline.saas.channel.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.channel.party.organisation.repository.OrganisationRepository
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.loan.model.db.LoanAgent
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl
import org.hibernate.type.YesNoType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.metamodel.Attribute

/**
 * @title: OrganisationService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:55
 */
@Service
class OrganisationService(
    private val organisationRepos: OrganisationRepository,
    private val tenantDateTime: TenantDateTime
) :
    BaseMultiTenantRepoService<Organisation, Long>(organisationRepos) {

    @Autowired
    private lateinit var sequence: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun registerOrganisation(dtoOrganisationAdd: DTOOrganisationAdd): DTOOrganisationView {
        val id = sequence.nextId()

        dtoOrganisationAdd.organisationIdentifications.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }
        dtoOrganisationAdd.businessUnits?.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }
        dtoOrganisationAdd.organizationInvolvements?.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }

        val organisation = Organisation(
            id = id,
            parentOrganisationId = dtoOrganisationAdd.parentOrganisationId?.toLong(),
            legalEntityIndicator = dtoOrganisationAdd.legalEntityIndicator,
            organisationSector = dtoOrganisationAdd.organisationSector,
            organisationRegistrationDate = dtoOrganisationAdd.organisationRegistrationDate?.run {
                tenantDateTime.toTenantDateTime(
                    this
                ).toDate()
            },
            placeOfRegistration = dtoOrganisationAdd.placeOfRegistration,
            enable = YesOrNo.Y
        )
        organisation.organisationIdentifications =
            objectMapper.convertValue(dtoOrganisationAdd.organisationIdentifications)
        dtoOrganisationAdd.businessUnits?.let { organisation.businessUnits = objectMapper.convertValue(it) }
        dtoOrganisationAdd.organizationInvolvements?.let {
            organisation.organizationInvolvements = objectMapper.convertValue(it)
        }

        dtoOrganisationAdd.channelCast?.let {
            organisation.channelCast = ChannelCast(
                id = id,
                channelCode = it.channelCode,
                channelName = it.channelName,
                channelCastType = it.channelCastType,
                dateTime = tenantDateTime.now().toDate()
            )
        }
        val save = save(organisation)

        return getDTOOrganisationView(save)
    }

    fun updateOrganisation(id: Long, dtoOrganisationChange: DTOOrganisationChange): DTOOrganisationView {
        val organisation = getOne(id) ?: throw OrganisationNotFoundException("Invalid organisation")
        dtoOrganisationChange.organisationIdentifications.forEach {
            it.id ?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }
        organisation.organisationIdentifications =
            objectMapper.convertValue(dtoOrganisationChange.organisationIdentifications)

        dtoOrganisationChange.businessUnits?.forEach {
            it.id ?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }

        dtoOrganisationChange.businessUnits?.let { organisation.businessUnits = objectMapper.convertValue(it) }

        dtoOrganisationChange.organizationInvolvements?.forEach {
            it.id ?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }
        dtoOrganisationChange.organizationInvolvements?.let {
            organisation.organizationInvolvements = objectMapper.convertValue(it)
        }

        return getDTOOrganisationView(save(organisation))
    }


    fun updateOrganisationEnable(id: Long): DTOOrganisationView {
        val organisation = getOne(id) ?: throw OrganisationNotFoundException("Invalid organisation")
        organisation.enable = if (organisation.enable == YesOrNo.Y) YesOrNo.N else YesOrNo.Y
        return getDTOOrganisationView(save(organisation))
    }

    fun getDetail(id: Long): DTOOrganisationView {
        val organisation = getOne(id) ?: throw OrganisationNotFoundException("Invalid organisation")

        return getDTOOrganisationView(organisation)
    }

    fun getOrganisationPaged(
        legalEntityIndicator: String?,
        organisationIdentification: String?,
        pageable: Pageable
    ): Page<DTOOrganisationView> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            legalEntityIndicator?.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<Long>("legalEntityIndicator"),
                        legalEntityIndicator
                    )
                )
            }
            organisationIdentification?.run {
                val connection =
                    root.join<Organisation, OrganisationIdentification>("organisationIdentifications", JoinType.INNER)
                predicates.add(
                    criteriaBuilder.equal(
                        connection.get<String>("organisationIdentification"),
                        organisationIdentification
                    )
                )
            }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable).map { getDTOOrganisationView(it) }
    }

    fun getDTOOrganisationView(organisation: Organisation): DTOOrganisationView {
        return DTOOrganisationView(
            id = organisation.id.toString(),
            legalEntityIndicator = organisation.legalEntityIndicator,
            organisationSector = organisation.organisationSector,
            organisationRegistrationDate = organisation.organisationRegistrationDate?.run {
                tenantDateTime.toTenantDateTime(
                    this
                ).toString()
            },
            parentOrganisationId = organisation.parentOrganisationId?.toString(),
            placeOfRegistration = organisation.placeOfRegistration,
            organisationIdentifications = objectMapper.convertValue(organisation.organisationIdentifications),
            organizationInvolvements = objectMapper.convertValue(organisation.organizationInvolvements),
            businessUnits = objectMapper.convertValue(organisation.businessUnits),
            tenantId = organisation.getTenantId(),
            channelCast = organisation.channelCast?.let { objectMapper.convertValue(it) },
            enable = organisation.enable
        )
    }

    fun getOrganisationListByEnable(enable: YesOrNo?): MutableList<DTOChannelCastView> {
        val paged = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            enable?.let { predicates.add(criteriaBuilder.equal(root.get<YesOrNo>("enable"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, Pageable.unpaged())
        return paged.content.filter { it.channelCast != null }.map {
            it.channelCast!!.let { channelCast ->
                DTOChannelCastView(
                    id = channelCast.id.toString(),
                    channelCode = channelCast.channelCode,
                    channelName = channelCast.channelName,
                    channelCastType = channelCast.channelCastType
                )
            }
        }.toMutableList()
    }

}