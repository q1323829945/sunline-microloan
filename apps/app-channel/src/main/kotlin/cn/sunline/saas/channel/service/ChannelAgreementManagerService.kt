package cn.sunline.saas.channel.service

import cn.sunline.saas.channel.agreement.exception.ChannelAgreementNotFoundException
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementPageView
import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementView
import cn.sunline.saas.channel.controller.dto.DTOChannelCommissionAgreementView
import cn.sunline.saas.channel.exception.ChannelAgreementBusinessException
import cn.sunline.saas.channel.exception.ChannelBusinessException
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.CommissionAmountRangeType
import cn.sunline.saas.global.constant.CommissionCountRangeType
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

        val oldAgreement = channelAgreementService.getOneByChannelIdAndAgreementType(
            dtoChannelAgreementAdd.channelId,
            dtoChannelAgreementAdd.agreementType
        )
        if (oldAgreement != null) {
            val now = tenantDateTime.now()
            if (now.isBefore(tenantDateTime.toTenantDateTime(oldAgreement.toDateTime))) {
                throw ChannelAgreementBusinessException(
                    "This channel has effective agreement",
                    ManagementExceptionCode.DATA_ALREADY_EXIST
                )
            }
        }
        dtoChannelAgreementAdd.channelCommissionArrangement.forEach {
            if (it.commissionMethodType == CommissionMethodType.APPROVAL_AMOUNT_RATIO || it.commissionMethodType == CommissionMethodType.APPLY_AMOUNT_RATIO) {
                val rangeType =
                    dtoChannelAgreementAdd.channelCommissionArrangement.groupBy { it.commissionAmountRangeType }
                rangeType.forEach {
                    val chooseSize = it.value.size
                    if (chooseSize > 1) {
                        throw ChannelAgreementBusinessException(
                            "more than one same config",
                            ManagementExceptionCode.DATA_ALREADY_EXIST
                        )
                    }
                }
                val size = dtoChannelAgreementAdd.channelCommissionArrangement.size
                val defaultSize =
                    dtoChannelAgreementAdd.channelCommissionArrangement.filter { it.commissionAmountRangeType == CommissionAmountRangeType.DEFAULT }.size
                if (size > 1 && defaultSize > 0) {
                    throw ChannelAgreementBusinessException(
                        "please choose default or others config",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
            }

            if (it.commissionMethodType == CommissionMethodType.APPROVAL_COUNT_FIX_AMOUNT || it.commissionMethodType == CommissionMethodType.APPLY_COUNT_FIX_AMOUNT) {
                val rangeType =
                    dtoChannelAgreementAdd.channelCommissionArrangement.groupBy { it.commissionCountRangeType }
                rangeType.forEach {
                    val chooseSize = it.value.size
                    if (chooseSize > 1) {
                        throw ChannelAgreementBusinessException(
                            "more than one same config",
                            ManagementExceptionCode.DATA_ALREADY_EXIST
                        )
                    }
                }
                val size = dtoChannelAgreementAdd.channelCommissionArrangement.size
                val defaultSize =
                    dtoChannelAgreementAdd.channelCommissionArrangement.filter { it.commissionCountRangeType == CommissionCountRangeType.DEFAULT }.size
                if (size > 1 && defaultSize > 0) {
                    throw ChannelAgreementBusinessException(
                        "please choose default or others config",
                        ManagementExceptionCode.DATA_ALREADY_EXIST
                    )
                }
            }
        }


        val channelCommissionAgreement = channelAgreementService.registered(dtoChannelAgreementAdd)
        val channelArrangements = mutableListOf<DTOChannelArrangementView>()
        channelCommissionAgreement.channelArrangement.forEach {
            channelArrangements += DTOChannelArrangementView(
                id = it.id.toString(),
                channelAgreementId = it.channelAgreementId.toString(),
                commissionMethodType = it.commissionMethodType,
                commissionType = it.commissionType,
                commissionAmount = it.commissionAmount,
                commissionRatio = it.commissionRatio,
                commissionAmountRangeType = it.commissionAmountRangeType,
                commissionCountRangeType = it.commissionCountRangeType
            )
        }
        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.channelAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelAgreement.channelId.toString(),
            channelArrangement = channelArrangements,
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

        val channelArrangements = mutableListOf<DTOChannelArrangementView>()
        detail.channelArrangement.forEach {
            channelArrangements += DTOChannelArrangementView(
                id = it.id.toString(),
                channelAgreementId = it.channelAgreementId.toString(),
                commissionMethodType = it.commissionMethodType,
                commissionType = it.commissionType,
                commissionAmount = it.commissionAmount,
                commissionRatio = it.commissionRatio,
                commissionAmountRangeType = it.commissionAmountRangeType,
                commissionCountRangeType = it.commissionCountRangeType
            )
        }

        return DTOChannelCommissionAgreementView(
            id = channelCommissionAgreement.id.toString(),
            channelId = channelCommissionAgreement.channelId.toString(),
            channelArrangement = channelArrangements,
            agreementType = channelCommissionAgreement.agreementType,
            fromDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(channelCommissionAgreement.toDateTime).toString()
        )
    }
}