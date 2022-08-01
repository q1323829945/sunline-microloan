package cn.sunline.saas.customer_offer.service

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.ApplyStatus.*
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferLoanView
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer.offer.exceptions.CustomerOfferNotFoundException
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferStatusException
import cn.sunline.saas.customer_offer.service.dto.DTOCustomerOfferPage
import cn.sunline.saas.customer_offer.service.dto.DTOInvokeCustomerOfferView
import cn.sunline.saas.customer_offer.service.dto.DTOManagementCustomerOfferView
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.global.constant.UnderwritingType.*
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse

@Service
class AppCustomerOfferService(
    val customerOfferInvoke: CustomerOfferInvoke,
    val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getPaged(customerId:Long?,productId:Long?,productName:String?,pageable: Pageable):Page<DTOCustomerOfferPage> {
        val filter = customerOfferService.getCustomerOfferPaged(customerId,productId,productName, pageable).content
            .filter { it.status != RECORD }

        val page = customerOfferService.rePaged(filter,pageable).map {
            val apply = customerLoanApplyService.getOne(it.id!!)
            val customerOfferLoanView = apply?.run {
                objectMapper.readValue<DTOCustomerOfferLoanView>(this.data)
            }
            val underwriting = customerOfferInvoke.getUnderwriting(it.id!!)

            val loanAgreement = underwriting?.status?.run {
                if(UnderwritingType.valueOf(underwriting.status) == APPROVAL){
                    customerOfferInvoke.getLoanAgreement(it.id!!)
                }else {
                    null
                }
            }

            DTOCustomerOfferPage(
                customerOfferId = it.id.toString(),
                userName = customerOfferLoanView?.detail?.name,
                amount = apply?.amount?.toString(),
                datetime = tenantDateTime.toTenantDateTime(it.datetime).toString(),
                productName = it.productName,
                status = it.status,
                term = customerOfferLoanView?.loan?.term,
                currency = customerOfferLoanView?.loan?.currency,
                underwritingType = underwriting?.status?.run { UnderwritingType.valueOf(underwriting.status) },
                loanAgreementType = loanAgreement?.run { this.status }
            )
        }


        return page
    }

    fun approval(id: Long){
        updateStatus(id,APPROVALED)

    }

    fun rejected(id:Long){
        updateStatus(id, ApplyStatus.REJECTED)
    }

    private fun updateStatus(id:Long,status: ApplyStatus){
        val customerOffer = customerOfferService.getOne(id)?:throw CustomerOfferNotFoundException("Invalid customer offer")

        checkStatus(customerOffer.status,status)

        customerOffer.status = status

        customerOfferService.save(customerOffer)
    }

    private fun checkStatus(oldStatus: ApplyStatus, newStatus: ApplyStatus){
        when(oldStatus){
            SUBMIT -> if(newStatus != APPROVALED && newStatus != ApplyStatus.REJECTED) throw CustomerOfferStatusException("status can not be update")
            else -> throw CustomerOfferStatusException("status can not be update")
        }
    }

    fun getDetail(id: Long): DTOManagementCustomerOfferView {
        val customerOfferLoan = customerLoanApplyService.retrieve(id)
        val managementCustomerOffer = objectMapper.convertValue<DTOManagementCustomerOfferView>(customerOfferLoan)

        managementCustomerOffer.guarantor?.run {
            guarantors.forEach {
                it.primaryGuarantor = it.nric == this.primaryGuarantor
            }
        }


        managementCustomerOffer.uploadDocument?.forEach {
            val config = loanUploadConfigureService.getOne(it.documentTemplateId.toLong())
            config?.run {
                it.documentTemplateName = this.name
            }
            it.fileName = it.file.substring(it.file.lastIndexOf("/")+1)
        }

        val customerOffer = customerOfferService.getOne(id)
        val product = customerOfferInvoke.getProduct(customerOffer!!.productId)
        managementCustomerOffer.product = objectMapper.convertValue(product)
        managementCustomerOffer.product!!.productId =product.id

        val underwriting = if(customerOffer.status != RECORD) customerOfferInvoke.getUnderwriting(customerOffer.id!!) else null

        underwriting?.run {
            managementCustomerOffer.underwriting = objectMapper.convertValue(this)
        }

        return managementCustomerOffer
    }


    fun download(path:String,response: HttpServletResponse){
        val inputStream = customerLoanApplyService.download(path)

        val fileName = path.substring(path.lastIndexOf("/")+1)

        response.reset()
        response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"))

        IOUtils.write(inputStream.readBytes(),response.outputStream)
        inputStream.close()
    }

    fun getInvokeCustomerOffer(id:Long): DTOInvokeCustomerOfferView {
        val customerOfferLoan = customerLoanApplyService.retrieve(id)

        val customerOffer = customerOfferService.getOne(id)?:throw CustomerOfferNotFoundException("Invalid customer offer")
        val product = customerOfferInvoke.getProduct(customerOffer.productId)

        return DTOInvokeCustomerOfferView(
            id.toString(),
            "123", //TODO:
            customerOffer.customerId.toString(),
            product.id,
            customerOfferLoan.loan!!.amount,
            customerOfferLoan.loan!!.currency,
            customerOfferLoan.loan!!.term,
            objectMapper.convertValue(customerOfferLoan.referenceAccount!!),
            product.loanPurpose
        )
    }
}