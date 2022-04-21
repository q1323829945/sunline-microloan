package cn.sunline.saas.disbursement.instruction.controllers

import cn.sunline.saas.disbursement.instruction.service.DisbursementInstructionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @title: DisbursementInstructionController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 15:17
 */
@RestController
@RequestMapping("/Disbursement")
class DisbursementInstructionController {

    @Autowired
    private lateinit var disbursementInstructionService: DisbursementInstructionService
}