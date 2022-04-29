package cn.sunline.saas.party.organisation.service

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.party.organisation.exception.OrganisationNotFoundException
import cn.sunline.saas.party.organisation.model.db.Organisation
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
import org.joda.time.Instant
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * @title: OrganisationService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:55
 */
@Service
class OrganisationService(private val organisationRepos: OrganisationRepository) :
    BaseMultiTenantRepoService<Organisation, Long>(organisationRepos) {

    @Autowired
    private lateinit var sequence: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun registerOrganisation(dtoOrganisationAdd: DTOOrganisationAdd):DTOOrganisationView{
        dtoOrganisationAdd.id = sequence.nextId()
        dtoOrganisationAdd.tenantId = ContextUtil.getTenant()
        dtoOrganisationAdd.organisationIdentifications.forEach {
            it.id = sequence.nextId()
            it.organisationId = dtoOrganisationAdd.id
            it.tenantId = ContextUtil.getTenant()
        }
        dtoOrganisationAdd.businessUnits.forEach {
            it.id = sequence.nextId()
            it.organisationId = dtoOrganisationAdd.id
            it.tenantId = ContextUtil.getTenant()
        }
        dtoOrganisationAdd.organizationInvolvements.forEach {
            it.id = sequence.nextId()
            it.organisationId = dtoOrganisationAdd.id
            it.tenantId = ContextUtil.getTenant()
        }

        val organisation = objectMapper.convertValue<Organisation>(dtoOrganisationAdd)
        organisation.organisationRegistrationDate = Instant.now()

        val save = save(organisation)

        return getDTOOrganisationView(save)
    }

    fun updateOrganisation(id:Long,dtoOrganisationChange: DTOOrganisationChange):DTOOrganisationView{
        val organisation = getOne(id)?:throw OrganisationNotFoundException("Invalid organisation")
        dtoOrganisationChange.organisationIdentifications.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
                it.tenantId = ContextUtil.getTenant()
            }
        }
        organisation.organisationIdentifications = objectMapper.convertValue(dtoOrganisationChange.organisationIdentifications)

        dtoOrganisationChange.businessUnits.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
                it.tenantId = ContextUtil.getTenant()
            }
        }
        organisation.businessUnits = objectMapper.convertValue(dtoOrganisationChange.businessUnits)


        dtoOrganisationChange.organizationInvolvements.forEach {
            it.id?: run {
                it.id = sequence.nextId().toString()
                it.organisationId = id.toString()
                it.tenantId = ContextUtil.getTenant()
            }
        }
        organisation.organizationInvolvements = objectMapper.convertValue(dtoOrganisationChange.organizationInvolvements)

        val save = save(organisation)

        return getDTOOrganisationView(save)
    }

    fun getDetail(id:Long):DTOOrganisationView{
        val organisation = getOne(id)?:throw OrganisationNotFoundException("Invalid organisation")

        return getDTOOrganisationView(organisation)
    }

    fun getOrganisationPaged(pageable: Pageable):Page<DTOOrganisationView>{
        return getPageWithTenant(null,pageable).map { getDTOOrganisationView(it) }
    }

    private fun getDTOOrganisationView(organisation: Organisation):DTOOrganisationView{
        return DTOOrganisationView(
            id = organisation.id.toString(),
            legalEntityIndicator = organisation.legalEntityIndicator,
            organisationSector = organisation.organisationSector,
            organisationRegistrationDate = organisation.organisationRegistrationDate.toString(),
            parentOrganisationId = organisation.parentOrganisationId?.toString(),
            placeOfRegistration = organisation.placeOfRegistration,
            organisationIdentifications = objectMapper.convertValue(organisation.organisationIdentifications),
            organizationInvolvements = objectMapper.convertValue(organisation.organizationInvolvements),
            businessUnits = objectMapper.convertValue(organisation.businessUnits),
            tenantId = organisation.getTenantId()
        )
    }
}