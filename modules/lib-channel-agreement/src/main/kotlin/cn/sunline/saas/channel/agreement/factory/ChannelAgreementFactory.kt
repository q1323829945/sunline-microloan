package cn.sunline.saas.channel.agreement.factory

import cn.sunline.saas.channel.agreement.model.db.ChannelAgreement
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChannelAgreementFactory(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(dtoChannelAgreementAdd: DTOChannelAgreementAdd): ChannelAgreement {
        val channelAgreementId = seq.nextId()
        val now = tenantDateTime.now()
        return ChannelAgreement(
            id = channelAgreementId,
            agreementType = dtoChannelAgreementAdd.agreementType,
            signedDate = now.toDate(),
            fromDateTime = if (dtoChannelAgreementAdd.fromDateTime.isNullOrEmpty()) now.toDate()
                else tenantDateTime.toTenantDateTime(dtoChannelAgreementAdd.fromDateTime).toDate(),
            toDateTime = if (dtoChannelAgreementAdd.toDateTime.isNullOrEmpty()) now.toDate()
                else tenantDateTime.toTenantDateTime(dtoChannelAgreementAdd.toDateTime).toDate(),
            version = 1,
            status = AgreementStatus.SIGNED,
            agreementDocument = null,
            channelId = dtoChannelAgreementAdd.channelId
        )
    }
}