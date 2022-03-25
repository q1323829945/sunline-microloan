package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.db.CustomerLoanApply
import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.repositories.CustomerLoanApplyRepository
import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.obs.api.DeleteParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class CustomerLoanApplyService (private val customerLoanApplyRepo: CustomerLoanApplyRepository) :
        BaseMultiTenantRepoService<CustomerLoanApply, Long>(customerLoanApplyRepo){

    data class DTOFile(
            val originalFilename:String,
            val inputStream: InputStream
    )
    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    @Autowired
    private lateinit var huaweiCloudTools: HuaweiCloudTools

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService



    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun submit(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){
        val customerOffer = customerOfferService.getOneById(customerOfferId)

        if(dtoFile.size != dtoCustomerOfferLoanAdd.uploadDocument.size){
            throw BusinessException("file upload failed",ManagementExceptionCode.FILE_UPLOAD_FAILED)
        }
        for(i in dtoCustomerOfferLoanAdd.uploadDocument.indices){
            val file = dtoFile[i]
            val uploadDocument = dtoCustomerOfferLoanAdd.uploadDocument[i]
            val key = "${customerOffer.customerId}/$customerOfferId/${uploadDocument.documentTemplateId}/${file.originalFilename}"
            huaweiCloudService.putObject(PutParams(huaweiCloudTools.bucketName,key,file.inputStream))
            dtoCustomerOfferLoanAdd.uploadDocument[i].file = key
        }

        val data = Gson().toJson(dtoCustomerOfferLoanAdd)
        this.save(CustomerLoanApply(customerOfferId,dtoCustomerOfferLoanAdd.loan.amount, data))
    }

    fun update(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){

        val noFileParams = dtoCustomerOfferLoanAdd.uploadDocument.filter { it.file == null }

        if(dtoFile.size != noFileParams.size){
            throw BusinessException("file upload failed",ManagementExceptionCode.FILE_UPLOAD_FAILED)
        }

        val customerLoanApply = this.getOne(customerOfferId)?:throw NotFoundException("Invalid loan apply")
        val originalData = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)
        dtoCustomerOfferLoanAdd.uploadDocument = originalData.uploadDocument

        val customerOffer = customerOfferService.getOneById(customerOfferId)

        for(i in noFileParams.indices){
            val file = dtoFile[i]
            dtoCustomerOfferLoanAdd.uploadDocument.forEach {
                if(it.documentTemplateId == noFileParams[i].documentTemplateId){
                    huaweiCloudService.deleteObject(DeleteParams(huaweiCloudTools.bucketName,it.file!!))
                    val key = "${customerOffer.customerId}/$customerOfferId/${it.documentTemplateId}/${file.originalFilename}"
                    huaweiCloudService.putObject(PutParams(huaweiCloudTools.bucketName,key,file.inputStream))
                    it.file = key
                }
            }
        }

        val data = Gson().toJson(dtoCustomerOfferLoanAdd)

        customerLoanApply.amount = dtoCustomerOfferLoanAdd.loan.amount
        customerLoanApply.data = data

        this.save(customerLoanApply)
    }


    fun retrieve(customerOfferId:Long,countryCode:String): DTOCustomerOfferLoanView{
        val customerLoanApply = this.getOne(customerOfferId)?:throw NotFoundException("Invalid loan apply")
        val dtoCustomerOfferLoanAdd = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)

        val customerOffer = customerOfferService.getOneById(customerOfferId)
        val dtoCustomerOffer = Gson().fromJson(customerOffer.data,DTOCustomerOfferAdd::class.java)
        val dTOCustomerOfferProcedureView = objectMapper.convertValue<DTOCustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)
        dTOCustomerOfferProcedureView.customerOfferId = customerOffer.id
        dTOCustomerOfferProcedureView.status = customerOffer.status

        return DTOCustomerOfferLoanView(
                dTOCustomerOfferProcedureView,
                null,
                DTOProductView(customerOffer.productId),
                dtoCustomerOfferLoanAdd.loan,
                dtoCustomerOfferLoanAdd.company,
                dtoCustomerOfferLoanAdd.contact,
                dtoCustomerOfferLoanAdd.detail,
                dtoCustomerOfferLoanAdd.guarantor,
                dtoCustomerOfferLoanAdd.financial,
                dtoCustomerOfferLoanAdd.uploadDocument,
                dtoCustomerOfferLoanAdd.kyc,
        )
    }

}