package cn.sunline.saas.channel.service

import cn.sunline.saas.channel.agreement.exception.ChannelAgreementNotFoundException
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementPageView
import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementView
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelCommissionItemsView
import cn.sunline.saas.channel.controller.dto.DTOChannelCommissionAgreementView
import cn.sunline.saas.channel.exception.ChannelAgreementBusinessException
import cn.sunline.saas.channel.exception.ChannelBusinessException
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
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

    fun addChannelCommissionAgreement(dtoChannelAgreementAdd: DTOChannelAgreementAdd): DTOChannelCommissionAgreementView {

        channelCastService.getOne(dtoChannelAgreementAdd.channelId) ?: throw ChannelBusinessException(
            "Invalid Channel", ManagementExceptionCode.CHANNEL_NOT_FOUND
        )

        val oldAgreement = channelAgreementService.getListByChannelIdAndAgreementType(
            dtoChannelAgreementAdd.channelId,
            dtoChannelAgreementAdd.agreementType
        )
        oldAgreement.forEach {
            val now = tenantDateTime.now()
            if (now.isBefore(tenantDateTime.toTenantDateTime(it.toDateTime))) {
                throw ChannelAgreementBusinessException(
                    "This channel has effective agreement",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }

        dtoChannelAgreementAdd.channelCommissionArrangement.commissionItems.forEach {
            if (dtoChannelAgreementAdd.channelCommissionArrangement.commissionMethodType == CommissionMethodType.AMOUNT_RATIO) {
                if (dtoChannelAgreementAdd.channelCommissionArrangement.commissionItems.any { f -> f.commissionAmountRange == null || f.commissionRatio == null }) {
                    throw ChannelAgreementBusinessException(
                        "amount range or ratio is not null",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
                val amountItem = dtoChannelAgreementAdd.channelCommissionArrangement.commissionItems.filter { f ->
                    f.applyStatus == it.applyStatus && f.commissionRatio == it.commissionRatio
                }
                if (amountItem.size > 1) {
                    throw ChannelAgreementBusinessException(
                        "more than one same config",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
            }

            if (dtoChannelAgreementAdd.channelCommissionArrangement.commissionMethodType == CommissionMethodType.COUNT_FIX_AMOUNT) {
                if (dtoChannelAgreementAdd.channelCommissionArrangement.commissionItems.any { f -> f.commissionCountRange == null || f.commissionAmount == null }) {
                    throw ChannelAgreementBusinessException(
                        "count range or amount is not null",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
                val amountItem = dtoChannelAgreementAdd.channelCommissionArrangement.commissionItems.filter { f ->
                    f.applyStatus == it.applyStatus && f.commissionAmount == it.commissionAmount
                }
                if (amountItem.size > 1) {
                    throw ChannelAgreementBusinessException(
                        "more than one same config",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
            }
        }


        val channelCommissionAgreement = channelAgreementService.registered(dtoChannelAgreementAdd)
        val dtoChannelCommissionItemsView = mutableListOf<DTOChannelCommissionItemsView>()
        channelCommissionAgreement.channelArrangement.commissionItems.forEach {
            dtoChannelCommissionItemsView += DTOChannelCommissionItemsView(
                id = it.id.toString(),
                channelArrangementId = it.channelArrangementId.toString(),
                applyStatus = it.applyStatus,
                commissionAmount = it.commissionAmount,
                commissionRatio = it.commissionRatio,
                commissionAmountRange = it.commissionAmountRange,
                commissionCountRange = it.commissionCountRange
            )
        }
        val channelArrangement = DTOChannelArrangementView(
            id = channelCommissionAgreement.channelArrangement.id.toString(),
            channelAgreementId = channelCommissionAgreement.channelArrangement.channelAgreementId.toString(),
            commissionMethodType = channelCommissionAgreement.channelArrangement.commissionMethodType,
            commissionType = channelCommissionAgreement.channelArrangement.commissionType,
            channelArrangementType = channelCommissionAgreement.channelArrangement.channelArrangementType,
            commissionItems = dtoChannelCommissionItemsView
        )
        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.channelAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelAgreement.channelId.toString(),
            channelArrangement = channelArrangement,
            agreementType = channelCommissionAgreement.channelAgreement.agreementType,
            fromDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.channelAgreement.fromDateTime)
                .toString(),
            toDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.channelAgreement.toDateTime)
                .toString()
        )
    }

    fun getChannelCommissionAgreement(id: Long): DTOChannelCommissionAgreementView {
        val channelCommissionAgreement = channelAgreementService.getOne(id) ?: throw ChannelAgreementNotFoundException(
            "This channel agreement has already exist"
        )
        val detail = channelAgreementService.getDetail(id)!!

        val dtoChannelCommissionItemsView = mutableListOf<DTOChannelCommissionItemsView>()
        detail.channelArrangement.commissionItems.forEach {
            dtoChannelCommissionItemsView += DTOChannelCommissionItemsView(
                id = it.id.toString(),
                channelArrangementId = it.channelArrangementId.toString(),
                applyStatus = it.applyStatus,
                commissionAmount = it.commissionAmount,
                commissionRatio = it.commissionRatio,
                commissionAmountRange = it.commissionAmountRange,
                commissionCountRange = it.commissionCountRange
            )
        }

        val channelArrangement = DTOChannelArrangementView(
            id = detail.channelArrangement.id.toString(),
            channelAgreementId = detail.channelArrangement.channelAgreementId.toString(),
            commissionMethodType = detail.channelArrangement.commissionMethodType,
            commissionType = detail.channelArrangement.commissionType,
            channelArrangementType = detail.channelArrangement.channelArrangementType,
            commissionItems = dtoChannelCommissionItemsView
        )

        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelId.toString(),
            channelArrangement = channelArrangement,
            agreementType = channelCommissionAgreement.agreementType,
            fromDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.toDateTime).toString()
        )
    }
}