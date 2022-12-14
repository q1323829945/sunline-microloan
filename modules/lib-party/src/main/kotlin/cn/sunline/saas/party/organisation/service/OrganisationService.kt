package cn.sunline.saas.party.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.organisation.exception.OrganisationNotFoundException
import cn.sunline.saas.party.organisation.model.db.Organisation
import cn.sunline.saas.party.organisation.model.db.OrganisationIdentification
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationChange
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.party.organisation.repository.OrganisationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

/**
 * @title: OrganisationService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:55
 */
@Service
class OrganisationService(private val organisationRepos: OrganisationRepository,
    private val tenantDateTime: TenantDateTime) :
    BaseMultiTenantRepoService<Organisation, Long>(organisationRepos) {

    @Autowired
    private lateinit var sequence: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun registerOrganisation(dtoOrganisationAdd: DTOOrganisationAdd):DTOOrganisationView{
        val id = sequence.nextId()

        dtoOrganisationAdd.organisationIdentifications.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }
        dtoOrganisationAdd.businessUnits.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }
        dtoOrganisationAdd.organizationInvolvements.forEach {
            it.id = sequence.nextId()
            it.organisationId = id
        }

        val organisation = Organisation(
            id = id,
            parentOrganisationId = dtoOrganisationAdd.parentOrganisationId?.toLong(),
            legalEntityIndicator = dtoOrganisationAdd.legalEntityIndicator,
            organisationSector = dtoOrganisationAdd.organisationSector,
            organisationRegistrationDate = dtoOrganisationAdd.organisationRegistrationDate?.run { tenantDateTime.toTenantDateTime(this).toDate() },
            placeOfRegistration = dtoOrganisationAdd.placeOfRegistration
        )
        organisation.organisationIdentifications = objectMapper.convertValue(dtoOrganisationAdd.organisationIdentifications)
        organisation.businessUnits = objectMapper.convertValue(dtoOrganisationAdd.businessUnits)
        organisation.organizationInvolvements = objectMapper.convertValue(dtoOrganisationAdd.organizationInvolvements)

        val save = save(organisation)

        return getDTOOrganisationView(save)
    }

    fun updateOrganisation(id:Long,dtoOrganisationChange: DTOOrganisationChange):DTOOrganisationView{
        val organisation = getOne(id)?:throw OrganisationNotFoundException("Invalid organisation")
        dtoOrganisationChange.organisationIdentifications.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }
        organisation.organisationIdentifications = objectMapper.convertValue(dtoOrganisationChange.organisationIdentifications)

        dtoOrganisationChange.businessUnits.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }
        organisation.businessUnits = objectMapper.convertValue(dtoOrganisationChange.businessUnits)

        dtoOrganisationChange.organizationInvolvements.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
            }
        }
        organisation.organizationInvolvements = objectMapper.convertValue(dtoOrganisationChange.organizationInvolvements)

        return getDTOOrganisationView(save(organisation))
    }

    fun getDetail(id:Long):DTOOrganisationView{
        val organisation = getOne(id)?:throw OrganisationNotFoundException("Invalid organisation")

        return getDTOOrganisationView(organisation)
    }

    fun getOrganisationPaged(legalEntityIndicator:String?,organisationIdentification: String?,pageable: Pageable):Page<DTOOrganisationView>{
        return getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            legalEntityIndicator?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("legalEntityIndicator"),legalEntityIndicator)) }
            organisationIdentification?.run {
                val connection = root.join<Organisation, OrganisationIdentification>("organisationIdentifications",JoinType.INNER)
                predicates.add(criteriaBuilder.equal(connection.get<String>("organisationIdentification"),organisationIdentification))
            }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable).map { getDTOOrganisationView(it) }
    }

    fun getDTOOrganisationView(organisation: Organisation):DTOOrganisationView{
        return DTOOrganisationView(
            id = organisation.id.toString(),
            legalEntityIndicator = organisation.legalEntityIndicator,
            organisationSector = organisation.organisationSector,
            organisationRegistrationDate = organisation.organisationRegistrationDate?.run { tenantDateTime.toTenantDateTime(this).toString() },
            parentOrganisationId = organisation.parentOrganisationId?.toString(),
            placeOfRegistration = organisation.placeOfRegistration,
            organisationIdentifications = objectMapper.convertValue(organisation.organisationIdentifications),
            organizationInvolvements = objectMapper.convertValue(organisation.organizationInvolvements),
            businessUnits = objectMapper.convertValue(organisation.businessUnits),
            tenantId = organisation.getTenantId()
        )
    }
}