package cn.sunline.saas.channel.arrangement.factory

import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
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
        dtoChannelArrangementAdd: List<DTOChannelArrangementAdd>
    ): List<ChannelArrangement> {
        val channelArrangements = mutableListOf<ChannelArrangement>()
        dtoChannelArrangementAdd.forEach {
            channelArrangements.add(
                ChannelArrangement(
                    id = seq.nextId(),
                    channelAgreementId = channelAgreementId,
                    applyStatus = it.applyStatus,
                    commissionType = it.commissionType,
                    commissionMethodType = it.commissionMethodType,
                    commissionAmountRange = it.commissionAmountRange,
                    commissionAmount = it.commissionAmount,
                    commissionCountRange = it.commissionCountRange,
                    commissionRatio = it.commissionRatio,
                 )
            )
        }
        return channelArrangements
    }
}