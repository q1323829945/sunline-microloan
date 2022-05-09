package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.db.CustomerLoanApply
import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.repositories.CustomerLoanApplyRepository
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.obs.api.DeleteParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.math.BigDecimal

@Service
class CustomerLoanApplyService (private val customerLoanApplyRepo: CustomerLoanApplyRepository) :
        BaseMultiTenantRepoService<CustomerLoanApply, Long>(customerLoanApplyRepo){

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    data class DTOFile(
            val originalFilename:String,
            val inputStream: InputStream
    )
    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

     fun submit(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){
        val customerOffer = customerOfferService.getOneById(customerOfferId)

        setUploadDocument(customerOfferId,customerOffer!!.customerId,dtoFile,dtoCustomerOfferLoanAdd.uploadDocument)

        val data = objectMapper.valueToTree<JsonNode>(dtoCustomerOfferLoanAdd).toPrettyString()

        val amount = dtoCustomerOfferLoanAdd.loan?.run {
            BigDecimal(dtoCustomerOfferLoanAdd.loan.amount)
        }

        val customerLoanApply = this.getOne(customerOfferId)?:CustomerLoanApply(customerOfferId,amount, data)
        customerLoanApply.data = data
        customerLoanApply.amount = amount

        this.save(customerLoanApply)
    }

     fun update(customerOfferId:Long, dtoCustomerOfferLoanChange: DTOCustomerOfferLoanChange, dtoFile: List<DTOFile>){
        val customerLoanApply = this.getOne(customerOfferId)?:throw NotFoundException("Invalid loan apply",ManagementExceptionCode.DATA_NOT_FOUND)

         val originalData = objectMapper.treeToValue<DTOCustomerOfferLoanChange>(objectMapper.readTree(customerLoanApply.data))

        val customerOffer = customerOfferService.getOneById(customerOfferId)

         dtoCustomerOfferLoanChange.uploadDocument?.run {
            val fileTemplateIdList = dtoCustomerOfferLoanChange.uploadDocument.map { it.documentTemplateId }
            originalData.uploadDocument?.forEach {
                if(!fileTemplateIdList.contains(it.documentTemplateId)){
                    dtoCustomerOfferLoanChange.uploadDocument.add(it)
                }
            }
        }

        setUploadDocument(customerOfferId,customerOffer!!.customerId,dtoFile,dtoCustomerOfferLoanChange.uploadDocument)

         val data = objectMapper.valueToTree<JsonNode>(dtoCustomerOfferLoanChange).toPrettyString()

         dtoCustomerOfferLoanChange.loan?.run {
            customerLoanApply.amount = BigDecimal(dtoCustomerOfferLoanChange.loan.amount)
        }
        customerLoanApply.data = data

        this.save(customerLoanApply)
    }


    private fun setUploadDocument(customerOfferId:Long, customerId:Long, dtoFile: List<DTOFile>, uploadDocumentList: List<DTOUploadDocument>?){
         dtoFile.forEach {  file ->
             val names = file.originalFilename.split("/")
             val templateId = 23229161248407552L
             val fileName = names[0]
//             val templateId = names[0].toLong()
//             val fileName = names[1]
             uploadDocumentList?.forEach{
                 if(templateId == it.documentTemplateId.toLong()){
                     it.file?.run {
                         huaweiCloudService.deleteObject(DeleteParams(this))
                     }
                     val key = "$customerId/$customerOfferId/$templateId/$fileName"
                     huaweiCloudService.putObject(PutParams(key,file.inputStream))
                     it.file = key
                 }
             }
        }
    }

    fun retrieve(customerOfferId: Long): DTOCustomerOfferLoanView {
        val customerLoanApply = this.getOne(customerOfferId) ?: return DTOCustomerOfferLoanView()

        return objectMapper.treeToValue(objectMapper.readTree(customerLoanApply.data))
    }

}
