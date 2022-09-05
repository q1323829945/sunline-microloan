package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.channel.controller.dto.DTOChannelCastView
import cn.sunline.saas.channel.controller.dto.DTOChannelIdentificationView
import cn.sunline.saas.channel.controller.dto.DTOChannelView
import cn.sunline.saas.channel.exception.ChannelBusinessException
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.channel.party.organisation.service.OrganisationService
import cn.sunline.saas.rpc.pubsub.impl.ChannelPublishImpl
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChannelSyncSchedulerTask(
    actorType:String = ActorType.SYNC_CHANNEL.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private var logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var organisationService: OrganisationService

    @Autowired
    private lateinit var channelPublishImpl: ChannelPublishImpl


    override fun doJob(actorId: String, jobId: String) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        try {
            val organisation = organisationService.getDetail(actorId.toLong())
            val dtoChannelView = getDTOChannelView(organisation)

            logger.info("[doJob]: sync $actorId channel start")

            channelPublishImpl.syncChannel(objectMapper.convertValue(dtoChannelView))


            logger.info("[doJob]: sync $actorId channel end")


        } catch (e: Exception) {
            //do nothing,until pass
            logger.error("[doJob]: sync applicationId:${actorId} channel, error massage : ${e.message}")
            return
        }

        logger.info("[doJob]: sync $actorId channel Success")
        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
        //delete reminder
        ActorReminderService.deleteReminders(actorType, actorId, jobId)
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
            tenantId = dtoOrganisationView.tenantId.toString()
        )
    }
}