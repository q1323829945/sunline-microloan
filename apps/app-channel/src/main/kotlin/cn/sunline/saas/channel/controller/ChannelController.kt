package cn.sunline.saas.channel.controller

import cn.sunline.saas.channel.agreement.model.dto.DTOChannelCommissionAgreementAdd
import cn.sunline.saas.channel.controller.dto.*
import cn.sunline.saas.channel.service.ChannelAgreementManagerService
import cn.sunline.saas.channel.service.ChannelManagerService
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("channel")
class ChannelController {

    @Autowired
    private lateinit var channelManagerService: ChannelManagerService

    @Autowired
    private lateinit var channelAgreementManagerService: ChannelAgreementManagerService

    private lateinit var channelCastViewList: List<ChannelCastView>

    init {
        val agent = ChannelCastView(ChannelCastType.AGENT, null, 1)
        val broker = ChannelCastView(ChannelCastType.BROKER, null, 2)
        channelCastViewList = listOf(agent, broker)
    }

    @GetMapping
    fun getChannelPaged(
        channelCode: String?,
        channelName: String?,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = channelManagerService.getChannelPaged(channelCode, channelName, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = channelManagerService.getAllChannel()
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOChannelView>> {
        val channel = channelManagerService.getChannel(id)
        return DTOResponseSuccess(channel).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoChannelAdd: DTOChannelAdd): ResponseEntity<DTOResponseSuccess<DTOChannelView>> {
        val channel = channelManagerService.addChannel(dtoChannelAdd)
        return DTOResponseSuccess(channel).response()
    }

    @PutMapping("{id}")
    fun updateOne(
        @PathVariable id: Long,
        @RequestBody dtoChannelChange: DTOChannelChange
    ): ResponseEntity<DTOResponseSuccess<DTOChannelView>> {
        val channel = channelManagerService.updateChannel(id, dtoChannelChange)
        return DTOResponseSuccess(channel).response()
    }

    @GetMapping("agreement/{channelId}")
    fun getAgreementPaged(
        @PathVariable channelId: Long,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = channelAgreementManagerService.getChannelAgreementPaged(channelId, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping("agreement/commission")
    fun addChannelCommissionAgreement(
        @RequestBody dtoChannelCommissionAgreementAdd: DTOChannelCommissionAgreementAdd
    ): ResponseEntity<DTOResponseSuccess<DTOChannelCommissionAgreementView>> {
        val channelAgreement =
            channelAgreementManagerService.addChannelCommissionAgreement(dtoChannelCommissionAgreementAdd)
        return DTOResponseSuccess(channelAgreement).response()
    }

    @GetMapping("agreement/commission/{agreementId}")
    fun getChannelCommissionAgreement(
        @PathVariable agreementId: Long,
    ): ResponseEntity<DTOResponseSuccess<DTOChannelCommissionAgreementView>> {
        val channelAgreement =
            channelAgreementManagerService.getChannelCommissionAgreement(agreementId)
        return DTOResponseSuccess(channelAgreement).response()
    }


    @GetMapping("channelCastType")
    fun getChannelCastList(): ResponseEntity<DTOResponseSuccess<List<ResultChannelCastType>>> {
        val result = ArrayList<ResultChannelCastType>()
        channelCastViewList.filter { it.parentName == null }.forEach {
            result += ResultChannelCastType(
                name = it.name,
                parentName = it.parentName,
                children = null,
                id = it.id
            )
        }
        return DTOResponseSuccess(result.toList()).response()
    }

    @GetMapping("agreement/agreementType")
    fun getChannelAgreementType(): ResponseEntity<DTOResponseSuccess<List<DTOChannelAgreementTypeView>>> {
        val list = ArrayList<DTOChannelAgreementTypeView>()
        list += DTOChannelAgreementTypeView(
            AgreementType.COMMISSION_SALE
        )
        return DTOResponseSuccess(list.toList()).response()
    }
}