package cn.sunline.saas.channel.template.data.service.impl

import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.channel.party.organisation.model.dto.DTOChannelCastAdd
import cn.sunline.saas.channel.party.organisation.model.dto.DTOChannelIdentificationAdd
import cn.sunline.saas.channel.template.data.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class ChannelTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    private fun getChannelCast(): DTOChannelCastAdd {
        return DTOChannelCastAdd(
            id = null,
            channelCode = "channel_" + sequence.nextId().toString().substring(6, 9),
            channelName = "channel_" + sequence.nextId().toString().substring(6, 9),
            channelCastType = ChannelCastType.AGENT
        )
    }

    private fun getChannelIdentification(): List<DTOChannelIdentificationAdd> {
        var listOf = listOf<DTOChannelIdentificationAdd>()
        listOf += DTOChannelIdentificationAdd(
            channelIdentificationType = OrganisationIdentificationType.BICFI,
            channelIdentification = "og" + sequence.nextId().toString().substring(6, 9)
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
            if (param.type.classifier == String::class) {
                mapData[param] = "channel_" + sequence.nextId().toString().substring(6, 9)
            }
            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }
            if (param.name!! == "channelCast") {
                mapData[param] = getChannelCast()
            }
            if (param.name!! == "channelIdentification") {
                mapData[param] = getChannelIdentification()
            }
        }

        return constructor.callBy(mapData)
    }
}