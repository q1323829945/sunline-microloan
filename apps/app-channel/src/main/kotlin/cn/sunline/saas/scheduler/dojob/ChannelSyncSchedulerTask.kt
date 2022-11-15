package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.channel.controller.dto.DTOChannelCastView
import cn.sunline.saas.channel.controller.dto.DTOChannelIdentificationView
import cn.sunline.saas.channel.controller.dto.DTOChannelView
import cn.sunline.saas.channel.party.organisation.exception.ChannelNotFoundException
import cn.sunline.saas.channel.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.channel.party.organisation.service.OrganisationService
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.bindings.impl.ChannelBindingsImpl
import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging

class ChannelSyncSchedulerTask(
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val organisationService: OrganisationService,
    private val channelBindingsImpl: ChannelBindingsImpl,
    actorType: String = ActorType.SYNC_CHANNEL.name,
    entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private var logger = KotlinLogging.logger {}


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        try {
            val organisation = organisationService.getDetail(actorId.toLong())
            val dtoChannelView = getDTOChannelView(organisation)

            logger.info("[doJob]: sync $actorId channel start")

           channelBindingsImpl.syncChannel(objectMapper.convertValue(dtoChannelView))


            logger.info("[doJob]: sync $actorId channel end")


        } catch (e: Exception) {
            //do nothing,until pass
            logger.error("[doJob]: sync applicationId:${actorId} channel, error massage : ${e.message}")
            return
        }

        logger.info("[doJob]: sync $actorId channel Success")
        schedulerJobHelper.succeed(schedulerJobLog)
        //delete reminder
        ActorReminderService.deleteReminders(actorType, actorId, jobId)
    }

    private fun getDTOChannelView(dtoOrganisationView: DTOOrganisationView): DTOChannelView {

        val channelCast = dtoOrganisationView.channelCast ?: throw ChannelNotFoundException("Channel Invalid")
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
}