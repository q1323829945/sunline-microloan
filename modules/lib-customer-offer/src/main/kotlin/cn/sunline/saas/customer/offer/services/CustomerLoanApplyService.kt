package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.db.CustomerLoanApply
import cn.sunline.saas.customer.offer.modules.dto.*
import cn.sunline.saas.customer.offer.repositories.CustomerLoanApplyRepository
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.global.constant.CountryType
import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.pdpa.factory.PDPAFactory
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
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
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    @Autowired
    private lateinit var huaweiCloudTools: HuaweiCloudTools

    @Autowired
    private lateinit var pdpaFactory: PDPAFactory

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService



    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun submit(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){

        if(dtoFile.size != dtoCustomerOfferLoanAdd.uploadDocument.size){
            throw Exception("data error")
        }
        for(i in dtoCustomerOfferLoanAdd.uploadDocument.indices){
            val file = dtoFile[i]
            val uploadDocument = dtoCustomerOfferLoanAdd.uploadDocument[i]
            val key = "$customerOfferId/${uploadDocument.documentTemplateId}/${file.originalFilename}"
            huaweiCloudService.putObject(PutParams(huaweiCloudTools.bucketName,key,file.inputStream))
            dtoCustomerOfferLoanAdd.uploadDocument[i].file = key
        }


        val data = Gson().toJson(dtoCustomerOfferLoanAdd)
        this.save(CustomerLoanApply(seq.nextId(),customerOfferId, data))
    }

    fun update(customerOfferId:Long, dtoCustomerOfferLoanAdd: DTOCustomerOfferLoanAdd, dtoFile: List<DTOFile>){
        val customerLoanApply = customerLoanApplyRepo.findByCustomerOfferId(customerOfferId)?:throw NotFoundException("Invalid loan apply")

        if(dtoFile.size != dtoCustomerOfferLoanAdd.uploadDocument.size){
            throw Exception("data error")
        }

        for(i in dtoCustomerOfferLoanAdd.uploadDocument.indices){
            val file = dtoFile[i]
            val uploadDocument = dtoCustomerOfferLoanAdd.uploadDocument[i]
            huaweiCloudService.putObject(PutParams(huaweiCloudTools.bucketName,"$customerOfferId/${uploadDocument.documentTemplateId}/${file.originalFilename}",file.inputStream))
            dtoCustomerOfferLoanAdd.uploadDocument[i].file = "$customerOfferId/${dtoFile[i].originalFilename}"
        }


        val data = Gson().toJson(dtoCustomerOfferLoanAdd)
        this.save(CustomerLoanApply(customerLoanApply.id,customerOfferId, data))
    }


    fun retrieve(customerOfferId:Long,countryCode:String): DTOCustomerOfferLoanView{
        val customerLoanApply = customerLoanApplyRepo.findByCustomerOfferId(customerOfferId)?:throw NotFoundException("Invalid loan apply")

        val dtoCustomerOfferLoanAdd = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)

        val pdpa = pdpaFactory.getInstance(CountryType.valueOf(countryCode)).getPDPA()

        val productId = customerOfferService.findProductIdById(customerOfferId)

        val customerOffer = customerOfferService.getOne(customerLoanApply.customerOfferId)?:throw NotFoundException("Invalid customer offer")
        val dtoCustomerOffer = Gson().fromJson(customerOffer.data,DTOCustomerOfferAdd::class.java)

        val dTOCustomerOfferProcedureView = objectMapper.convertValue<DTOCustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)
        dTOCustomerOfferProcedureView.customerOfferId = customerLoanApply.customerOfferId
        dTOCustomerOfferProcedureView.status = customerOffer.status

        return DTOCustomerOfferLoanView(
                dTOCustomerOfferProcedureView,
                pdpa,
                DTOProductView(productId,null,null,null,null,null,null),
                dtoCustomerOfferLoanAdd.loan!!,
                dtoCustomerOfferLoanAdd.company!!,
                dtoCustomerOfferLoanAdd.contact!!,
                dtoCustomerOfferLoanAdd.detail!!,
                dtoCustomerOfferLoanAdd.guarantor!!,
                dtoCustomerOfferLoanAdd.financial!!,
                dtoCustomerOfferLoanAdd.uploadDocument,
                dtoCustomerOfferLoanAdd.kyc!!,
        )
    }


    fun findAmountByCustomerOfferId(customerOfferId: Long):BigDecimal{
        val customerLoanApply = customerLoanApplyRepo.findByCustomerOfferId(customerOfferId)?:throw NotFoundException("Invalid loan apply")
        val dtoCustomerOfferLoanAdd = Gson().fromJson(customerLoanApply.data,DTOCustomerOfferLoanAdd::class.java)

        return dtoCustomerOfferLoanAdd.loan!!.amount
    }


}
