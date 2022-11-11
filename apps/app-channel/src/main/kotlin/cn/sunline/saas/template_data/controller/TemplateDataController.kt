package cn.sunline.saas.template_data.controller

import cn.sunline.saas.channel.agreement.model.dto.DTOChannelAgreementAdd
import cn.sunline.saas.channel.party.organisation.model.dto.DTOChannelAdd
import cn.sunline.saas.channel.product.model.dto.DTOProductAdd
import cn.sunline.saas.channel.product.model.dto.DTOQuestionnaireAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOPermission
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTORoleChange
import cn.sunline.saas.channel.rbac.modules.dto.DTOUserAdd
import cn.sunline.saas.channel.template.data.service.impl.*
import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.controller.dto.DTORatePlan
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("templateData")
class TemplateDataController {

    @Autowired
    private lateinit var interestRateTemplateDataServiceImpl: InterestRateTemplateDataServiceImpl

    @Autowired
    private lateinit var loanProductTemplateServiceImpl: LoanProductTemplateServiceImpl

    @Autowired
    private lateinit var commonTemplateDataServiceImpl: CommonTemplateDataServiceImpl

    @Autowired
    private lateinit var channelTemplateDataServiceImpl: ChannelTemplateDataServiceImpl

    @Autowired
    private lateinit var channelCommissionTemplateDataServiceImpl: CommissionTemplateDataServiceImpl

    @Autowired
    private lateinit var papdTemplateDataServiceImpl: PapdTemplateDataServiceImpl



    @GetMapping("ratePlan")
    fun getRatePlanData(): ResponseEntity<DTOResponseSuccess<DTORatePlan>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTORatePlan>(
                DTORatePlan::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("interestRate/{ratePlanId}")
    fun getInterestRate(@PathVariable(name = "ratePlanId") ratePlanId: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRate>> {
        return DTOResponseSuccess(
            interestRateTemplateDataServiceImpl.getTemplateData<DTOInterestRate>(
                DTOInterestRate::class,
                ratePlanId.toString(),
                false
            )
        ).response()
    }

    @GetMapping("questionnaire")
    fun getQuestionnaire(): ResponseEntity<DTOResponseSuccess<DTOQuestionnaireAdd>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTOQuestionnaireAdd>(
                DTOQuestionnaireAdd::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("loanProduct")
    fun getLoanProductData(): ResponseEntity<DTOResponseSuccess<DTOProductAdd>> {
        return DTOResponseSuccess(
            loanProductTemplateServiceImpl.getTemplateData<DTOProductAdd>(
                DTOProductAdd::class,
                null,
                false
            )
        ).response()
    }


    @GetMapping("channel")
    fun getOrganisationData(): ResponseEntity<DTOResponseSuccess<DTOChannelAdd>> {
        return DTOResponseSuccess(
            channelTemplateDataServiceImpl.getTemplateData<DTOChannelAdd>(
                DTOChannelAdd::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("channel/commission/{channelId}")
    fun getPersonData(@PathVariable(name = "channelId") channelId: Long): ResponseEntity<DTOResponseSuccess<DTOChannelAgreementAdd>> {
        return DTOResponseSuccess(
            channelCommissionTemplateDataServiceImpl.getTemplateData<DTOChannelAgreementAdd>(
                DTOChannelAgreementAdd::class,
                channelId.toString(),
                false
            )
        ).response()
    }

    @GetMapping("pdpa")
    fun getPdpaData(): ResponseEntity<DTOResponseSuccess<DTOPdpaAdd>> {
        return DTOResponseSuccess(
            papdTemplateDataServiceImpl.getTemplateData<DTOPdpaAdd>(
                DTOPdpaAdd::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("permission")
    fun getPermissionData(): ResponseEntity<DTOResponseSuccess<DTOPermission>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTOPermission>(
                DTOPermission::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("position")
    fun getPositionData(): ResponseEntity<DTOResponseSuccess<DTOPositionAdd>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTOPositionAdd>(
                DTOPositionAdd::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("role")
    fun getRoleData(): ResponseEntity<DTOResponseSuccess<DTORoleChange>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTORoleChange>(
                DTORoleChange::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("user")
    fun getUserData(): ResponseEntity<DTOResponseSuccess<DTOUserAdd>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.getTemplateData<DTOUserAdd>(
                DTOUserAdd::class,
                null,
                false
            )
        ).response()
    }

//    @GetMapping("activity/definition")
//    fun getActivityDefinitionData(): ResponseEntity<DTOResponseSuccess<DTOActivityDefinition>> {
//        return DTOResponseSuccess(
//            riskControlRuleTemplateDataServiceImpl.getTemplateData<DTOActivityDefinition>(
//                DTOActivityDefinition::class,
//                null,
//                false
//            )
//        ).response()
//    }
//
//
//    @GetMapping("event/definition")
//    fun getEventDefinitionData(): ResponseEntity<DTOResponseSuccess<DTOEventDefinition>> {
//        return DTOResponseSuccess(
//            commonTemplateDataServiceImpl.getTemplateData<DTOEventDefinition>(
//                DTOEventDefinition::class,
//                null,
//                false
//            )
//        ).response()
//    }
//
//    @GetMapping("process/definition")
//    fun getProcessDefinitionData(): ResponseEntity<DTOResponseSuccess<DTOProcessDefinition>> {
//        return DTOResponseSuccess(
//            commonTemplateDataServiceImpl.getTemplateData<DTOProcessDefinition>(
//                DTOProcessDefinition::class,
//                null,
//                false
//            )
//        ).response()
//    }
}