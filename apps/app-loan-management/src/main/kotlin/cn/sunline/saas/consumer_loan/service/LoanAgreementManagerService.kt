package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.exception.LoanAgreementNotFoundException
import cn.sunline.saas.consumer_loan.service.dto.DTOLoanAgreementView
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LoanAgreementManagerService {

    @Autowired
    private lateinit var agreementService: LoanAgreementService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getPaged(pageable: Pageable):Page<DTOLoanAgreementView>{
        return agreementService.getPageWithTenant(null,pageable).map {
            DTOLoanAgreementView(
                id = it.id.toString(),
                agreementType = it.agreementType,
                signedDate = it.signedDate.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd"),
                fromDateTime = it.fromDateTime.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd"),
                toDateTime = it.toDateTime.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd"),
                term = it.term,
                version = it.version,
                status = it.status,
                amount = it.amount.toPlainString(),
                currency = it.currency,
                productId = it.productId.toString(),
                agreementDocument = it.agreementDocument,
                purpose = it.purpose,
                applicationId = it.applicationId.toString(),
                userId = it.userId.toString(),
                involvements = objectMapper.convertValue(it.involvements)
            )
        }
    }

    fun paid(id:Long){
        val loanAgreement = agreementService.getOne(id)?:throw LoanAgreementNotFoundException("Invalid loan agreement")

        loanAgreement.status = AgreementStatus.PAID

        agreementService.save(loanAgreement)
    }
}