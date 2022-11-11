package cn.sunline.saas.template_data.controller

import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.controller.dto.DTORatePlan
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loanuploadconfigure.controller.dto.DTOUploadConfigure
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.rbac.controller.PermissionController
import cn.sunline.saas.rbac.controller.dto.DTORoleChange
import cn.sunline.saas.rbac.controller.dto.DTOUserAdd
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.risk.control.rule.controller.dto.DTORiskControlRuleAdd
import cn.sunline.saas.tempalte.data.service.impl.*
import cn.sunline.saas.tempalte.data.service.impl.InterestRateTemplateDataServiceImpl
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
    private lateinit var loanProductTemplateDataServiceImpl: LoanProductTemplateDataServiceImpl

    @Autowired
    private lateinit var commonTemplateDataServiceImpl: CommonTemplateDataServiceImpl

    @Autowired
    private lateinit var organisationTemplateDataServiceImpl: OrganisationTemplateDataServiceImpl

    @Autowired
    private lateinit var personTemplateDataServiceImpl: PersonTemplateDataServiceImpl

    @Autowired
    private lateinit var papdTemplateDataServiceImpl: PapdTemplateDataServiceImpl

    @Autowired
    private lateinit var riskControlRuleTemplateDataServiceImpl: RiskControlRuleTemplateDataServiceImpl


    @GetMapping("ratePlan")
    fun getRatePlanData(): ResponseEntity<DTOResponseSuccess<DTORatePlan>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.deepCustomAssignment(
                DTORatePlan::class,
                null
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

    @GetMapping("loanProduct")
    fun getLoanProductData(): ResponseEntity<DTOResponseSuccess<DTOLoanProduct>> {
        return DTOResponseSuccess(
            loanProductTemplateDataServiceImpl.getTemplateData<DTOLoanProduct>(
                DTOLoanProduct::class,
                null,
                false
            )
        ).response()
    }


    @GetMapping("uploadConfigure")
    fun getUploadConfigureData(): ResponseEntity<DTOResponseSuccess<DTOUploadConfigure>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.deepCustomAssignment(
                DTOUploadConfigure::class,
                null
            )
        ).response()
    }

    @GetMapping("organisation")
    fun getOrganisationData(): ResponseEntity<DTOResponseSuccess<DTOOrganisationAdd>> {
        return DTOResponseSuccess(
            organisationTemplateDataServiceImpl.getTemplateData<DTOOrganisationAdd>(
                DTOOrganisationAdd::class,
                null,
                false
            )
        ).response()
    }

    @GetMapping("person")
    fun getPersonData(): ResponseEntity<DTOResponseSuccess<DTOPersonAdd>> {
        return DTOResponseSuccess(
            personTemplateDataServiceImpl.getTemplateData<DTOPersonAdd>(
                DTOPersonAdd::class,
                null,
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
    fun getPermissionData(): ResponseEntity<DTOResponseSuccess<PermissionController.DTOPermission>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.deepCustomAssignment(
                PermissionController.DTOPermission::class,
                null
            )
        ).response()
    }

    @GetMapping("role")
    fun getRoleData(): ResponseEntity<DTOResponseSuccess<DTORoleChange>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.deepCustomAssignment(
                DTORoleChange::class,
                null
            )
        ).response()
    }

    @GetMapping("user")
    fun getUserData(): ResponseEntity<DTOResponseSuccess<DTOUserAdd>> {
        return DTOResponseSuccess(
            commonTemplateDataServiceImpl.deepCustomAssignment(
                DTOUserAdd::class,
                null
            )
        ).response()
    }

    @GetMapping("riskControl")
    fun getRiskControl(): ResponseEntity<DTOResponseSuccess<DTORiskControlRuleAdd>> {
        return DTOResponseSuccess(
            riskControlRuleTemplateDataServiceImpl.getTemplateData<DTORiskControlRuleAdd>(
                DTORiskControlRuleAdd::class,
                null,
                false
            )
        ).response()
    }
}