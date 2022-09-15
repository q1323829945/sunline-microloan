package cn.sunline.saas.channel.service

import cn.sunline.saas.channel.controller.dto.*
import cn.sunline.saas.channel.controller.dto.DTOChannelCastView
import cn.sunline.saas.channel.exception.ChannelBusinessException
import cn.sunline.saas.channel.party.organisation.model.dto.*
import cn.sunline.saas.channel.party.organisation.model.dto.DTOChannelCastAdd
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.channel.party.organisation.service.OrganisationService
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerDetail
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.bindings.dto.DTOChannelData
import cn.sunline.saas.rpc.bindings.impl.ChannelBindingsImpl
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.create.CreateScheduler
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class ChannelManagerService(private val tenantDateTime: TenantDateTime) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private var logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var organisationService: OrganisationService

    @Autowired
    private lateinit var channelCastService: ChannelCastService

    @Autowired
    private lateinit var channelBindingsImpl: ChannelBindingsImpl

    @Autowired
    private lateinit var createScheduler: CreateScheduler

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    fun getChannelPaged(channelCode: String?, channelName: String?, pageable: Pageable): Page<DTOChannelPageView> {
        val page = channelCastService.getChannelCastPaged(channelCode, channelName, pageable)
        return page.map {
            val organisation = organisationService.getOne(it.id) ?: throw ChannelBusinessException("Invalid Channel", ManagementExceptionCode.CHANNEL_NOT_FOUND)
            DTOChannelPageView(
                 id = it.id.toString(),
                channelCode = it.channelCode,
                channelName = it.channelName,
                channelCastType = it.channelCastType,
                enable = organisation.enable
            )
        }
    }

    fun getAllChannel(): Page<DTOChannelCastView> {
        val page = channelCastService.getPaged(pageable = Pageable.unpaged())
        return page.map { objectMapper.convertValue(it) }
    }

    fun addChannel(dtoChannelAdd: DTOChannelAdd): DTOChannelView {

        val oldChannel = channelCastService.getChannelCast(
            dtoChannelAdd.channelCast.channelCode,
            dtoChannelAdd.channelCast.channelName
        )
        if (oldChannel != null) {
            throw ChannelBusinessException("This channel has already exist", ManagementExceptionCode.DATA_ALREADY_EXIST)
        }

        val dtoOrganisationAdd = getDTOOrganisationAdd(dtoChannelAdd)
        val newOrganisation = organisationService.registerOrganisation(dtoOrganisationAdd)

        val newChannel = getDTOChannelView(newOrganisation)

        addChannelStatistics(newChannel)

        syncChannel("addChannel", newChannel, false)

        return newChannel
    }

    fun updateChannel(id: Long, dtoChannelChange: DTOChannelChange): DTOChannelView {

        val dtoOrganisationChange = getDTOOrganisationChange(id, dtoChannelChange)
        val updateOrganisation = organisationService.updateOrganisation(id, dtoOrganisationChange)
        return getDTOChannelView(updateOrganisation)
    }

    fun updateChannelEnable(id: Long): DTOChannelView {
        val updateOrganisation = organisationService.updateOrganisationEnable(id)
        val updateChannel = getDTOChannelView(updateOrganisation)

        syncChannel("updateChannel", updateChannel, true)
        return updateChannel
    }


    fun getChannel(id: Long): DTOChannelView {
        val organisation = organisationService.getDetail(id)
        return getDTOChannelView(organisation)
    }

    private fun getDTOChannelView(dtoOrganisationView: DTOOrganisationView): DTOChannelView {

        val channelCast = dtoOrganisationView.channelCast ?: throw ChannelBusinessException(
            "Channel Invalid",
            ManagementExceptionCode.CHANNEL_NOT_FOUND
        )
        val dtoChannelCast = DTOChannelCastView(
            id = channelCast.id.toString(),
            channelCode = channelCast.channelCode,
            channelName = channelCast.channelName,
            channelCastType = channelCast.channelCastType
        )

        val dtoChannelIdentificationViews = ArrayList<DTOChannelIdentificationView>()
        dtoOrganisationView.organisationIdentifications.forEach {
            dtoChannelIdentificationViews += DTOChannelIdentificationView(
                id = it.id.toString(),
                channelId = it.organisationId.toString(),
                channelIdentificationType = it.organisationIdentificationType,
                channelIdentification = it.organisationIdentification
            )
        }

        return DTOChannelView(
            id = dtoOrganisationView.id,
            legalEntityIndicator = dtoOrganisationView.legalEntityIndicator,
            organisationSector = dtoOrganisationView.organisationSector,
            organisationRegistrationDate = dtoOrganisationView.organisationRegistrationDate?.let {
                tenantDateTime.toTenantDateTime(it).toString()
            },
            placeOfRegistration = dtoOrganisationView.placeOfRegistration,
            channelCast = dtoChannelCast,
            channelIdentification = dtoChannelIdentificationViews,
            tenantId = dtoOrganisationView.tenantId.toString(),
            enable = dtoOrganisationView.enable
        )
    }

    private fun getDTOOrganisationAdd(dtoChannelAdd: DTOChannelAdd): DTOOrganisationAdd {

        val channelCast = DTOChannelCastAdd(
            id = null,
            channelCode = dtoChannelAdd.channelCast.channelCode,
            channelName = dtoChannelAdd.channelCast.channelName,
            channelCastType = dtoChannelAdd.channelCast.channelCastType
        )
        val dtoOrganisationIdentificationAdds = ArrayList<DTOOrganisationIdentificationAdd>()
        dtoChannelAdd.channelIdentification.forEach {
            dtoOrganisationIdentificationAdds += DTOOrganisationIdentificationAdd(
                id = null,
                organisationId = null,
                organisationIdentificationType = it.channelIdentificationType,
                organisationIdentification = it.channelIdentification
            )
        }
        return DTOOrganisationAdd(
            id = null,
            legalEntityIndicator = dtoChannelAdd.legalEntityIndicator,
            organisationSector = dtoChannelAdd.organisationSector,
            organisationRegistrationDate = dtoChannelAdd.organisationRegistrationDate,
            parentOrganisationId = null,
            placeOfRegistration = dtoChannelAdd.placeOfRegistration,
            organisationIdentifications = dtoOrganisationIdentificationAdds,
            organizationInvolvements = null,
            businessUnits = null,
            channelCast = channelCast
        )
    }

    private fun getDTOOrganisationChange(id: Long, dtoChannelChange: DTOChannelChange): DTOOrganisationChange {

        val dtoOrganisationIdentificationChange = ArrayList<DTOOrganisationIdentificationChange>()
        dtoChannelChange.channelIdentification.forEach {
            dtoOrganisationIdentificationChange += DTOOrganisationIdentificationChange(
                id = it.id,
                organisationId = it.channelId,
                organisationIdentificationType = it.channelIdentificationType,
                organisationIdentification = it.channelIdentification
            )
        }
        return DTOOrganisationChange(
            organisationIdentifications = dtoOrganisationIdentificationChange,
            organizationInvolvements = null,
            businessUnits = null,
        )
    }

    private fun syncChannel(method: String, dtoChannelView: DTOChannelView, isUpdate: Boolean) {
        try {
            logger.info("[${method}]: sync ${dtoChannelView.id} channel start")

            val data = DTOChannelData(
                channelCode = dtoChannelView.channelCast.channelCode,
                channelName = dtoChannelView.channelCast.channelName,
                enable = dtoChannelView.enable,
                isUpdate = isUpdate
            )

            channelBindingsImpl.syncChannel(data)

            logger.info("[${method}]: sync ${dtoChannelView.id} channel end")
        } catch (e: Exception) {
            logger.error("[${method}]: sync applicationId:${dtoChannelView.id} channel, error massage : ${e.message}")
            createScheduler.create(ActorType.SYNC_CHANNEL, dtoChannelView.id)
        }
    }

    private fun addChannelStatistics(dtoChannelView: DTOChannelView) {
        try {
            logger.info("[addChannelStatistics]: add ${dtoChannelView.id} channel statistics detail start")

            customerDetailService.getOneByPartyIdAndPartyType(dtoChannelView.id.toLong(), PartyType.CHANNEL) ?: run {
                customerDetailService.saveCustomerDetail(
                    DTOCustomerDetail(
                        partyId = dtoChannelView.id.toLong(),
                        partyType = PartyType.CHANNEL
                    )
                )
            }

            logger.info("[addChannelStatistics]: add ${dtoChannelView.id} channel statistics detail end")
        } catch (e: Exception) {
            logger.error("[addChannelStatistics]: add applicationId:${dtoChannelView.id} channel statistics detail, error massage : ${e.message}")
            createScheduler.create(ActorType.CHANNEL_STATISTICS, dtoChannelView.id)
        }
    }

}