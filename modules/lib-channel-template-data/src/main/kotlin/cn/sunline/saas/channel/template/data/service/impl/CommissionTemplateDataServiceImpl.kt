package cn.sunline.saas.channel.template.data.service.impl


import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelCommissionItemsAdd
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.channel.template.data.excpetion.TemplateDataBusinessException
import cn.sunline.saas.channel.template.data.service.TemplateDataService
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class CommissionTemplateDataServiceImpl : TemplateDataService() {


    @Autowired
    private lateinit var channelCastService: ChannelCastService

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    fun <T : Any> getTemplateData(type: KClass<T>, channelId: String, overrideDefaults: Boolean): T {
        val channel =
            channelCastService.getOne(channelId.toLong()) ?: throw TemplateDataBusinessException("Invalid channel")
        val map = mutableMapOf<String, Any>()
        map["channelId"] = channelId.toLong()
        map["agreementType"] = AgreementType.COMMISSION_SALE
        map["fromDateTime"] = tenantDateTime.toTenantDateTime(tenantDateTime.now().toString()).toString()
        map["toDateTime"] = tenantDateTime.toTenantDateTime(tenantDateTime.now().plusYears(1).toString()).toString()
        val commissionItems = mutableListOf<DTOChannelCommissionItemsAdd>()
        commissionItems += DTOChannelCommissionItemsAdd(
            applyStatus = ApplyStatus.APPROVALED,
            commissionAmount = BigDecimal("200"),
            commissionRatio = null,
            commissionAmountRange = null,
            commissionCountRange = 2000L

        )
        val dtoChannelArrangementAdd = DTOChannelArrangementAdd(
            commissionMethodType = CommissionMethodType.COUNT_FIX_AMOUNT,
            commissionType = CommissionType.LOAN_APPLICATION,
            channelArrangementType = ChannelArrangementType.COMMISSION,
            commissionItems = commissionItems

        )
        map["channelCommissionArrangement"] = dtoChannelArrangementAdd
        return getTemplateData(type, map, overrideDefaults)
    }


    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (defaultMapData != null && defaultMapData.containsKey(param.name!!)) {
                mapData[param] = defaultMapData[param.name!!]
            } else {
                if (param.type.classifier == String::class) {
                    mapData[param] = "channel_" + sequence.nextId().toString().substring(6, 9)
                }
                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
            }
        }

        return constructor.callBy(mapData)
    }
}