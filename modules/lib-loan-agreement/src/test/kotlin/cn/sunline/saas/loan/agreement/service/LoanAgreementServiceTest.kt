package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @title: LoanAgreementServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 15:58
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanAgreementServiceTest {

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Test
    fun `loan agreement save`() {
        val dtoLoanAgreementAdd =
            DTOLoanAgreementAdd(term = LoanTermType.SIX_MONTHS, amount = "1000000", currency = "CNY")
        val actual = loanAgreementService.registered(dtoLoanAgreementAdd)

        assertThat(actual).isNotNull
    }
}