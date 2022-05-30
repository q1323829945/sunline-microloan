package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.service.dto.DTOLoanAgreementView
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanAgreement
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LoanAgreementManagerService(
    val tenantDateTime: TenantDateTime,
    val loanAgreementPublish: LoanAgreementPublish
) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getPaged(pageable: Pageable):Page<DTOLoanAgreementView>{

        //TODO:
//        val paged = customerLoanInvoke.getLoanAgreementPaged(pageable).toPage().map {
//            objectMapper.convertValue<DTOLoanAgreementView>(it)
//        }

        return PageImpl(mutableListOf(),pageable,0)

//        return agreementService.getPageWithTenant(null,pageable).map {
//            DTOLoanAgreementView(
//                id = it.id.toString(),
//                agreementType = it.agreementType,
//                signedDate = tenantDateTime.toTenantDateTime(it.signedDate).toString(),
//                fromDateTime = tenantDateTime.toTenantDateTime(it.fromDateTime).toString(),
//                toDateTime = tenantDateTime.toTenantDateTime(it.toDateTime).toString(),
//                term = it.term,
//                version = it.version,
//                status = it.status,
//                amount = it.amount.toPlainString(),
//                currency = it.currency,
//                productId = it.productId.toString(),
//                agreementDocument = it.agreementDocument,
//                purpose = it.purpose,
//                applicationId = it.applicationId.toString(),
//                userId = it.userId.toString(),
//                involvements = objectMapper.convertValue(it.involvements)
//            )
//        }
    }

    fun paid(applicationId:String,status:AgreementStatus){

        loanAgreementPublish.updateLoanAgreementStatus(DTOLoanAgreement(
            applicationId,status
        ))

        //TODO:
//        customerLoanPublish.updateLoanAgreementStatus(DTOLoanAgreement(
//            id,
//            status
//        ))
    }
}