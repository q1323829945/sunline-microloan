package cn.sunline.saas.service

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.service.LoanApplyHandleService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanApplyHandleServiceTest {
    @Autowired
    private lateinit var loanApplyHandleService: LoanApplyHandleService

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("0")

        loanApplyHandleService.saveOne(
            DTOLoanApplyHandle(
                applicationId = "123",
                supplement = "admin"
            )
        )
    }

    @Test
    fun `update one`(){
        loanApplyHandleService.saveOne(
            DTOLoanApplyHandle(
                applicationId = "123",
                supplement = "qqqqqqq"
            )
        )
    }


}