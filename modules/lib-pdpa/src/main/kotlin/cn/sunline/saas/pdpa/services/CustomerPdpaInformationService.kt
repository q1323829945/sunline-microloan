package cn.sunline.saas.pdpa.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.pdpa.exception.CustomerPdpaInformationNotFoundException
import cn.sunline.saas.pdpa.modules.db.CustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformationChange
import cn.sunline.saas.pdpa.repositories.CustomerPdpaInformationRepository
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class CustomerPdpaInformationService (
    private val customerPdpaInformationRepository: CustomerPdpaInformationRepository
): BaseMultiTenantRepoService<CustomerPdpaInformation, Long>(customerPdpaInformationRepository) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getAndRegisterCustomerPdpaInformation(id:Long):CustomerPdpaInformation{
        var customerPdpaInformation = getOne(id)

        if(customerPdpaInformation == null){
            customerPdpaInformation = createCustomerPdpaInformation(
                DTOCustomerPdpaInformation(
                    id.toString()
                )
            )
        }

        return customerPdpaInformation
    }

    private fun createCustomerPdpaInformation(dtoCustomerPdpaInformation: DTOCustomerPdpaInformation):CustomerPdpaInformation{
        val customerPdpaInformation = objectMapper.convertValue<CustomerPdpaInformation>(dtoCustomerPdpaInformation)
        return save(customerPdpaInformation)
    }

    fun confirm(id:Long,dtoCustomerPdpaInformationChange: DTOCustomerPdpaInformationChange):CustomerPdpaInformation{
        return confirmAndWithdraw(id,dtoCustomerPdpaInformationChange)
    }

    fun withdraw(id:Long):CustomerPdpaInformation{
        return confirmAndWithdraw(id,DTOCustomerPdpaInformationChange(id.toString()))
    }

    private fun confirmAndWithdraw(id:Long,dtoCustomerPdpaInformationChange: DTOCustomerPdpaInformationChange):CustomerPdpaInformation{
        val customerPdpaInformation = getOne(id)?: throw CustomerPdpaInformationNotFoundException("Invalid customer inifo")
        customerPdpaInformation.pdpaId = dtoCustomerPdpaInformationChange.pdpaId?.toLong()
        customerPdpaInformation.fingerprint = dtoCustomerPdpaInformationChange.fingerprint
        customerPdpaInformation.electronicSignature = dtoCustomerPdpaInformationChange.electronicSignature
        customerPdpaInformation.faceRecognition = dtoCustomerPdpaInformationChange.faceRecognition
        return save(customerPdpaInformation)
    }
}