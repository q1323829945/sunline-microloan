package cn.sunline.saas.position_keeping.controller

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.position_keeping.service.PositionKeepingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("PositionKeeping")
class PositionKeepingController {
    @Autowired
    private lateinit var positionKeepingService: PositionKeepingService

    @PostMapping
    fun register(@RequestBody dtoBankingTransaction: DTOBankingTransaction){
        positionKeepingService.initialPositionKeeping(dtoBankingTransaction)
    }

    @PostMapping("/repay")
    fun repay(@RequestBody dtoBankingTransaction: DTOBankingTransaction){
        positionKeepingService.reducePositionKeeping(dtoBankingTransaction)
    }

}