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
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.math.BigDecimal

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
    private lateinit var customerOfferService: CustomerOfferService

     fun submit(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){
        val customerOffer = customerOfferService.getOneById(customerOfferId)

        setUploadDocument(customerOfferId,customerOffer!!.customerId,dtoFile,dtoCustomerOfferLoanAdd.uploadDocument)

        val data = Gson().toJson(dtoCustomerOfferLoanAdd)

        val amount = dtoCustomerOfferLoanAdd.loan?.run {
            BigDecimal(dtoCustomerOfferLoanAdd.loan.amount)
        }

        val customerLoanApply = this.getOne(customerOfferId)?:CustomerLoanApply(customerOfferId,amount, data)
        customerLoanApply.data = data
        customerLoanApply.amount = amount

        this.save(customerLoanApply)
    }

     fun update(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){
        val customerLoanApply = this.getOne(customerOfferId)?:throw NotFoundException("Invalid loan apply",ManagementExceptionCode.DATA_NOT_FOUND)
        val originalData = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)

        val customerOffer = customerOfferService.getOneById(customerOfferId)

        dtoCustomerOfferLoanAdd.uploadDocument?.run {
            val fileTemplateIdList = dtoCustomerOfferLoanAdd.uploadDocument.map { it.documentTemplateId }
            originalData.uploadDocument?.forEach {
                if(!fileTemplateIdList.contains(it.documentTemplateId)){
                    dtoCustomerOfferLoanAdd.uploadDocument.add(it)
                }
            }
        }

        setUploadDocument(customerOfferId,customerOffer!!.customerId,dtoFile,dtoCustomerOfferLoanAdd.uploadDocument)

        val data = Gson().toJson(dtoCustomerOfferLoanAdd)

        dtoCustomerOfferLoanAdd.loan?.run {
            customerLoanApply.amount = BigDecimal(dtoCustomerOfferLoanAdd.loan.amount)
        }
        customerLoanApply.data = data

        this.save(customerLoanApply)
    }


    private fun setUploadDocument(customerOfferId:Long, customerId:Long, dtoFile: List<DTOFile>, uploadDocumentList: List<DTOUploadDocument>?){
         dtoFile.forEach {  file ->
             val names = file.originalFilename.split("/")
             val templateId = names[0].toLong()
             val fileName = names[1]
             uploadDocumentList?.forEach{
                 if(templateId == it.documentTemplateId){
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

    fun retrieve(customerOfferId:Long): DTOCustomerOfferLoanView{
        val customerLoanApply = this.getOne(customerOfferId)?:return DTOCustomerOfferLoanView()

        val dtoCustomerOfferLoanAdd = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)

        return DTOCustomerOfferLoanView(
                null,
                null,
                null,
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
