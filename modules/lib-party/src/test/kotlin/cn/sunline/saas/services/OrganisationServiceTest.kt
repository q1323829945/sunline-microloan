package cn.sunline.saas.services

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.party.organisation.model.BusinessUnitType
import cn.sunline.saas.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.party.organisation.model.OrganizationInvolvementType
import cn.sunline.saas.party.organisation.model.dto.*
import cn.sunline.saas.party.organisation.service.OrganisationService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrganisationServiceTest(@Autowired val organisationService: OrganisationService,@Autowired val tenantService: TenantService) {

    var id = 0L

    @BeforeAll
    fun `init`(){
        tenantService.save(
            Tenant(
                id = 12344566,
                name = "name",
                country = CountryType.CHN,
                enabled = true,
                saasUUID = UUID.randomUUID()
            )
        )

        ContextUtil.setTenant("12344566")

        val organisation = organisationService.registerOrganisation(
            DTOOrganisationAdd(
                id = null,
                legalEntityIndicator = "legalEntityIndicator",
                organisationSector = "organisationSector",
                organisationRegistrationDate = "20220628",
                parentOrganisationId = null,
                placeOfRegistration = "placeOfRegistration",
                organisationIdentifications = mutableListOf(
                    DTOOrganisationIdentificationAdd(
                        id = null,
                        organisationId = null,
                        organisationIdentificationType = OrganisationIdentificationType.ELF,
                        organisationIdentification = "1111",
                    )
                ),
                organizationInvolvements = mutableListOf(
                    DTOOrganizationInvolvementAdd(
                        id = null,
                        organisationId = null,
                        organizationInvolvementType = OrganizationInvolvementType.EXECUTIVE_OFFICER,
                        partyId = 1
                    )
                ),
                businessUnits = listOf(
                    DTOBusinessUnitAdd(
                        id = null,
                        organisationId = null,
                        type = BusinessUnitType.MICRO_LOAN
                    )
                )
            )
        )

        id = organisation.id.toLong()
    }

    @Test
    @Transactional
    fun `get organisation`(){
        val organisation = organisationService.getDetail(id)

        Assertions.assertThat(organisation).isNotNull
    }

    @Test
    @Transactional
    fun `get paged`(){
        val paged = organisationService.getOrganisationPaged(null,null, Pageable.unpaged())
        Assertions.assertThat(paged.content.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun `update one`(){
        val updateOne = organisationService.updateOrganisation(
            id,
            DTOOrganisationChange(
                organisationIdentifications = listOf(
                    DTOOrganisationIdentificationChange(
                        id = null,
                        organisationId = id.toString(),
                        organisationIdentificationType = OrganisationIdentificationType.BICFI,
                        organisationIdentification = "55555",
                    ),
                ),
                organizationInvolvements = listOf(
                    DTOOrganizationInvolvementChange(
                        id = null,
                        organisationId = id.toString(),
                        organizationInvolvementType = OrganizationInvolvementType.SHAREHOLDER,
                        partyId = 1
                    )
                ),
                businessUnits = listOf(
                    DTOBusinessUnitChange(
                        id = null,
                        organisationId = id.toString(),
                        type = BusinessUnitType.MICRO_LOAN
                    )
                ),
            )
        )

        Assertions.assertThat(updateOne.organisationIdentifications[0].organisationIdentification).isEqualTo("55555")
    }
}