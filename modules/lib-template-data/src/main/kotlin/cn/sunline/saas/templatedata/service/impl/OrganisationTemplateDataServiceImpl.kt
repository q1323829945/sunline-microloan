package cn.sunline.saas.templatedata.service.impl

import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOInterestFeature
import cn.sunline.saas.loan.product.model.dto.DTORepaymentFeature
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.organisation.model.BusinessUnitType
import cn.sunline.saas.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.party.organisation.model.OrganizationInvolvementType
import cn.sunline.saas.party.organisation.model.dto.DTOBusinessUnitAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationIdentificationAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganizationInvolvementAdd
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.templatedata.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

@Service
class OrganisationTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    private fun getDTOOrganisationIdentification(): List<DTOOrganisationIdentificationAdd> {
        val listOf = mutableListOf<DTOOrganisationIdentificationAdd>()
        listOf += DTOOrganisationIdentificationAdd(
            id = null,
            organisationId = null,
            organisationIdentificationType = OrganisationIdentificationType.BICFI,
            organisationIdentification = "org_" + sequence.nextId().toString().substring(6, 9)
        )
        return listOf
    }

    private fun getDTOOrganizationInvolvement(): List<DTOOrganizationInvolvementAdd> {
        val listOf = mutableListOf<DTOOrganizationInvolvementAdd>()
        listOf += DTOOrganizationInvolvementAdd(
            id = null,
            organisationId = null,
            organizationInvolvementType = OrganizationInvolvementType.SHAREHOLDER,
            partyId = sequence.nextId()
        )
        return listOf
    }

    private fun getDTOBusinessUnit(): List<DTOBusinessUnitAdd> {
        val listOf = mutableListOf<DTOBusinessUnitAdd>()
        listOf += DTOBusinessUnitAdd(
            id = null,
            organisationId = null,
            type = BusinessUnitType.MICRO_LOAN
        )
        return listOf
    }


    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()

        constructor.parameters.forEach { param ->

            if (param.type.classifier == Long::class) {
                mapData[param] = sequence.nextId()
                if (param.name!! == "id") {
                    mapData[param] = null
                }
            }
            if (param.type.classifier == String::class) {
                if (param.name!! == "id") {
                    mapData[param] = null
                } else {
                    mapData[param] = "org_" + sequence.nextId().toString().substring(6, 9)
                }
            }
            if (param.name!! == "organisationRegistrationDate") {
                mapData[param] = tenantDateTime.getYearMonthDay(tenantDateTime.now())
            }
            if (param.name!! == "organisationIdentifications") {
                mapData[param] = getDTOOrganisationIdentification()
            }
            if (param.name!! == "organizationInvolvements") {
                mapData[param] = getDTOOrganizationInvolvement()
            }
            if (param.name!! == "businessUnits") {
                mapData[param] = getDTOBusinessUnit()
            }
        }

        return constructor.callBy(mapData)

    }
}