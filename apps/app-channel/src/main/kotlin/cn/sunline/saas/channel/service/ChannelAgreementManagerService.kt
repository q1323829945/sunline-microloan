package cn.sunline.saas.channel.service

import cn.sunline.saas.channel.agreement.exception.ChannelAgreementNotFoundException
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementPageView
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelCommissionAgreementAdd
import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.controller.dto.DTOChannelCommissionAgreementView
import cn.sunline.saas.channel.exception.ChannelAgreementBusinessException
import cn.sunline.saas.channel.exception.ChannelBusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class ChannelAgreementManagerService(private val tenantDateTime: TenantDateTime) {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var channelAgreementService: ChannelAgreementService

    @Autowired
    private lateinit var channelCastService: ChannelCastService


    fun getChannelAgreementPaged(channelId: Long, pageable: Pageable): Page<DTOChannelAgreementPageView> {
        val page = channelAgreementService.getPageByChannelId(channelId, pageable)
        return page.map { objectMapper.convertValue(it) }
    }

    fun addChannelCommissionAgreement(dtoChannelCommissionAgreementAdd: DTOChannelCommissionAgreementAdd): DTOChannelCommissionAgreementView {

        channelCastService.getOne(dtoChannelCommissionAgreementAdd.channelId) ?: throw ChannelBusinessException(
            "Invalid Channel", ManagementExceptionCode.CHANNEL_NOT_FOUND
        )

        val oldAgreement = channelAgreementService.getOneByChannelIdAndAgreementType(
            dtoChannelCommissionAgreementAdd.channelId,
            dtoChannelCommissionAgreementAdd.agreementType
        )
        if (oldAgreement != null) {
            val now = tenantDateTime.now()
            if(now.isBefore(tenantDateTime.toTenantDateTime(oldAgreement.toDateTime))){
                throw ChannelAgreementBusinessException(
                    "This channel has effective agreement",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }
        val dtoChannelAgreementAdd = DTOChannelAgreementAdd(
            channelId = dtoChannelCommissionAgreementAdd.channelId,
            agreementType = dtoChannelCommissionAgreementAdd.agreementType,
            fromDateTime = dtoChannelCommissionAgreementAdd.fromDateTime,
            toDateTime = dtoChannelCommissionAgreementAdd.toDateTime,
            channelArrangement = DTOChannelArrangementAdd(
                commissionMethodType = CommissionMethodType.RATIO,
                commissionAmount = null,
                commissionRatio = dtoChannelCommissionAgreementAdd.commissionRatio,
                commissionType = CommissionType.LOANAPPLICATION
            )
        )
        val channelCommissionAgreement = channelAgreementService.registered(dtoChannelAgreementAdd)
        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.channelAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelAgreement.channelId.toString(),
            commissionRatio = channelCommissionAgreement.channelArrangement.commissionRatio,
            agreementType = channelCommissionAgreement.channelAgreement.agreementType,
            fromDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.channelAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.channelAgreement.toDateTime).toString()
        )
    }

    fun getChannelCommissionAgreement(id: Long): DTOChannelCommissionAgreementView {
        val channelCommissionAgreement = channelAgreementService.getOne(id) ?: throw ChannelAgreementNotFoundException(
            "This channel agreement has already exist"
        )
        val detail = channelAgreementService.getDetail(id)!!
        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelId.toString(),
            commissionRatio = detail.channelArrangement.commissionRatio,
            agreementType = channelCommissionAgreement.agreementType,
            fromDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.toDateTime).toString()

        )
    }
}