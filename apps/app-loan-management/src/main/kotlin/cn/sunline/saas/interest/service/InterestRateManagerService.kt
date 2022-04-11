package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.dto.DTOInterestRateAdd
import cn.sunline.saas.interest.dto.DTOInterestRateChange
import cn.sunline.saas.interest.dto.DTOInterestRateView
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


interface InterestRateManagerService {

    fun getPaged(ratePlanId: Long?, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>

    fun addOne(dtoInterestRate: DTOInterestRateAdd): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>>

    fun updateOne(id: Long, dtoInterestRate: DTOInterestRateChange): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>>

    fun deleteOne(id: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>>
}