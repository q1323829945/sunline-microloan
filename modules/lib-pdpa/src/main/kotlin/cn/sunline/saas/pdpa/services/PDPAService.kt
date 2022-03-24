package cn.sunline.saas.pdpa.services

import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import cn.sunline.saas.pdpa.modules.db.PDPA
import cn.sunline.saas.pdpa.repositories.PDPARepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import java.io.InputStream

@Service
class PDPAService (private val pdpaRepo: PDPARepository) :
        BaseMultiTenantRepoService<PDPA, Long>(pdpaRepo){

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var obsApi: ObsApi

    @Autowired
    private lateinit var huaweiCloudTools: HuaweiCloudTools

    fun addPDPA(pdpa: PDPA):PDPA{
        pdpa.id = sequence.nextId()
        return this.save(pdpa)
    }


    fun sign(id: Long,fileName:String,inputStream:InputStream){
        val pdpa = this.getOne(id)?:throw NotFoundException("Invalid pdpa")
        val key = "${pdpa.customerId}/signature/$fileName"
        obsApi.putObject(PutParams(huaweiCloudTools.bucketName,key,inputStream))
        pdpa.signature = key
        this.save(pdpa)
    }

    fun getPDPAByCustomerId(id:Long):PDPA{
        return pdpaRepo.findByCustomerId(id)?:throw NotFoundException("Invalid pdpa")
    }
}