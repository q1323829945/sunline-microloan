package cn.sunline.saas.financial.accounting.controllers

import cn.sunline.saas.financial.accounting.service.FinancialAccountingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @title: FinancialAccountingController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 11:06
 */
@RestController
@RequestMapping("/FinancialAccounting")
class FinancialAccountingController {

    @Autowired
    private lateinit var financialAccountingService: FinancialAccountingService


}