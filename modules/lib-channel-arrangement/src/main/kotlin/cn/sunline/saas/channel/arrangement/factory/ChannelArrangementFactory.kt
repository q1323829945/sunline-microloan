package cn.sunline.saas.channel.arrangement.factory

import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.db.ChannelCommissionItems
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelCommissionItemsAdd
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ChannelArrangementFactory(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(
        channelAgreementId: Long,
        dtoChannelArrangementAdd: DTOChannelArrangementAdd
    ): ChannelArrangement {

        val channelArrangementId = seq.nextId()
        val channelCommissionItems = mutableListOf<ChannelCommissionItems>()
        dtoChannelArrangementAdd.commissionItems.forEach {
            channelCommissionItems.add(
                ChannelCommissionItems(
                    id = seq.nextId(),
                    channelArrangementId = channelArrangementId,
                    applyStatus = it.applyStatus,
                    commissionAmountRange = it.commissionAmountRange,
                    commissionAmount = it.commissionAmount,
                    commissionCountRange = it.commissionCountRange,
                    commissionRatio = it.commissionRatio,
                )
            )
        }
        return ChannelArrangement(
            id = channelArrangementId,
            channelAgreementId = channelAgreementId,
            commissionType = dtoChannelArrangementAdd.commissionType,
            commissionMethodType = dtoChannelArrangementAdd.commissionMethodType,
            channelArrangementType = dtoChannelArrangementAdd.channelArrangementType,
            commissionItems = channelCommissionItems
        )
    }
}