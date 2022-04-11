package cn.sunline.saas.interest.controller

import cn.sunline.saas.interest.dto.DTOInterestRateAdd
import cn.sunline.saas.interest.dto.DTOInterestRateChange
import cn.sunline.saas.interest.dto.DTOInterestRateView
import cn.sunline.saas.interest.service.InterestRateManagerService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("InterestRate")
class InterestRateController {


    @Autowired
    private lateinit var interestRateManagerService: InterestRateManagerService

    @GetMapping
    fun getPaged(
        @RequestParam(required = false) ratePlanId: Long?,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        return interestRateManagerService.getPaged(ratePlanId,pageable)
    }

    @PostMapping
    fun addOne(@RequestBody dtoInterestRate: DTOInterestRateAdd): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        return interestRateManagerService.addOne(dtoInterestRate)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoInterestRate: DTOInterestRateChange): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        return interestRateManagerService.updateOne(id,dtoInterestRate)
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        return interestRateManagerService.deleteOne(id)
    }
}