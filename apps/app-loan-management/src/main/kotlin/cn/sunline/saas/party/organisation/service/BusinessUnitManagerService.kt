package cn.sunline.saas.party.organisation.service

import cn.sunline.saas.party.organisation.service.dto.DTOBusinessUnitPaged
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BusinessUnitManagerService {
    @Autowired
    private lateinit var organisationService: OrganisationService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getList():List<DTOBusinessUnitPaged>{
        return organisationService.getPageWithTenant(null, Pageable.unpaged()).map {
            DTOBusinessUnitPaged(
                it.id.toString(),
                it.organisationSector,
                objectMapper.convertValue(it.businessUnits)
            )
        }.content
    }
}