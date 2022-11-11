package cn.sunline.saas.template.data.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.model.RoleType
import cn.sunline.saas.party.person.model.dto.DTOPersonIdentificationAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonNameAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonRoleAdd
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.template.data.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class PersonTemplateDataServiceImpl : TemplateDataService() {
    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    private fun getPersonName(): DTOPersonNameAdd {
        return DTOPersonNameAdd(
            id = null,
            firstName = "fp_" + sequence.nextId().toString().substring(6, 9),
            familyName = "fap_" + sequence.nextId().toString().substring(6, 9),
            givenName = "gp_" + sequence.nextId().toString().substring(6, 9)
        )
    }

    private fun getDTOPersonIdentification(): List<DTOPersonIdentificationAdd> {
        var listOf = mutableListOf<DTOPersonIdentificationAdd>()
        listOf += DTOPersonIdentificationAdd(
            id = null,
            personId = null,
            personIdentificationType = PersonIdentificationType.IDENTITY_CARD_NUMBER,
            personIdentification = "p_" + sequence.nextId().toString().substring(6, 9)
        )
        return listOf
    }

    private fun getDTOPersonRoles(): List<DTOPersonRoleAdd> {
        var listOf = mutableListOf<DTOPersonRoleAdd>()
        listOf += DTOPersonRoleAdd(
            id = null,
            personId = null,
            type = RoleType.INNER
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
            }
            if (param.type.classifier == String::class) {
                mapData[param] = "org_" + sequence.nextId().toString().substring(6, 9)
            }
            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }
            if (param.name!! == "organisationRegistrationDate") {
                mapData[param] = tenantDateTime.getYearMonthDay(tenantDateTime.now())
            }
            if (param.name!! == "personName") {
                mapData[param] = getPersonName()
            }
            if (param.name!! == "birthDate") {
                mapData[param] = tenantDateTime.getYearMonthDay(tenantDateTime.now())
            }
            if (param.name!! == "personIdentifications") {
                mapData[param] = getDTOPersonIdentification()
            }
            if (param.name!! == "personRoles") {
                mapData[param] = getDTOPersonRoles()
            }
        }

        return constructor.callBy(mapData)

    }
}