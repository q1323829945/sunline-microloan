package cn.sunline.saas.channel.interest.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.interest.controller.dto.DTORatePlan
import cn.sunline.saas.interest.controller.dto.DTORatePlanWithInterestRates
import cn.sunline.saas.interest.exception.RatePlanBusinessException
import cn.sunline.saas.interest.exception.RatePlanNotFoundException
import cn.sunline.saas.channel.interest.model.RatePlan
import cn.sunline.saas.channel.interest.model.RatePlanType
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
class RatePlanManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    fun getPaged(pageable: Pageable): Page<RatePlan> {
        return ratePlanService.getPageWithTenant(pageable = pageable)
    }

    fun getAll(type: RatePlanType, pageable: Pageable): Page<RatePlan> {
        return ratePlanService.getByRatePlanType(type, Pageable.unpaged())
    }

    fun addOne(dtoRatePlan: DTORatePlan): DTORatePlanWithInterestRates {
        val ratePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val typeRatePlan = ratePlanService.findByType(RatePlanType.STANDARD)
        if(typeRatePlan != null && ratePlan.type == RatePlanType.STANDARD){
            throw RatePlanBusinessException("The standard type of rate plan has exist，Only one is allowed")
        }
        val savedRatePlan = ratePlanService.addOne(ratePlan)
        return objectMapper.convertValue(savedRatePlan)
    }

    fun updateOne(id: Long,dtoRatePlan: DTORatePlan): DTORatePlanWithInterestRates {
        val oldRatePlan = ratePlanService.getOne(id)?: throw RatePlanNotFoundException("Invalid ratePlan")
        val newRatePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val savedRatePlan = ratePlanService.updateOne(oldRatePlan, newRatePlan)
        return objectMapper.convertValue(savedRatePlan)
    }


    fun getOne(id: Long): DTORatePlanWithInterestRates {
        val ratePlan = ratePlanService.getOne(id)?: throw RatePlanNotFoundException("Invalid ratePlan")
        return objectMapper.convertValue(ratePlan)
    }

    fun getInvokeAll(type: RatePlanType, pageable: Pageable): Page<RatePlan> {
        return ratePlanService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<RatePlanType>("type"), type))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }
}