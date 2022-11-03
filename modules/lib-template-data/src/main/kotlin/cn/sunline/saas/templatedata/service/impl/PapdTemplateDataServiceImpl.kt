package cn.sunline.saas.templatedata.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.templatedata.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class PapdTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime


    data class DTOPdpaAdd(

        val pdpaInformation: List<DTOPdpaItem>
    )

    private fun getDTOPersonRoles(): List<DTOPdpaItem> {
        var listOf = mutableListOf<DTOPdpaItem>()
        val mutableListOf = mutableListOf<DTOPdpaInformation>()
        mutableListOf += DTOPdpaInformation(
            label = "label" + sequence.nextId().toString().substring(6, 9),
            name = "name" + sequence.nextId().toString().substring(6, 9)
        )
        listOf += DTOPdpaItem(
            item = "item" + sequence.nextId().toString().substring(6, 9),
            information = mutableListOf,
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
                mapData[param] = "papd_" + sequence.nextId().toString().substring(6, 9)
            }
            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }
            if (param.name!! == "pdpaInformation") {
                mapData[param] = getDTOPersonRoles()
            }

        }

        return constructor.callBy(mapData)
    }
}