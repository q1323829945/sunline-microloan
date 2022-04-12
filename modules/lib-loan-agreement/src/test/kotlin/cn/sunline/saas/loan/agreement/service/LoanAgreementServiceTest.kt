package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTOPrepaymentArrangementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

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
        val planRates = mutableListOf<InterestRate>()
        val rate1 = InterestRate(id = 1, LoanTermType.SIX_MONTHS, rate = BigDecimal("7.2"), ratePlanId = 1)
        val rate2 = InterestRate(id = 2, LoanTermType.ONE_YEAR, rate = BigDecimal("12.5"), ratePlanId = 1)
        planRates.add(rate1)
        planRates.add(rate2)
        val dtoInterestArrangementAdd = DTOInterestArrangementAdd(
            InterestType.FIXED, BaseYearDays.ACTUAL_YEAR, "1",
            "150", planRates
        )

        val prepaymentArrangement = mutableListOf<DTOPrepaymentArrangementAdd>()
        val dtoPrepaymentArrangementAdd1 =
            DTOPrepaymentArrangementAdd(LoanTermType.ONE_MONTH, PrepaymentType.NOT_ALLOWED, BigDecimal("150"))
        prepaymentArrangement.add(dtoPrepaymentArrangementAdd1)
        val dtoRepaymentArrangementAdd = DTORepaymentArrangementAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.SIX_MONTHS, RepaymentDayType.BASE_LOAN_DAY, prepaymentArrangement
        )
        val feeArrangement = mutableListOf<DTOFeeArrangementAdd>()
        val dtoFeeArrangementAdd = DTOFeeArrangementAdd(
            "test", FeeMethodType.FIX_AMOUNT, BigDecimal("100"), null,
            FeeDeductType.IMMEDIATE
        )
        feeArrangement.add(dtoFeeArrangementAdd)

        val lender = mutableListOf<Long>()
        lender.add(2)

        val dtoLoanAgreementAdd =
            DTOLoanAgreementAdd(
                productId = 1,
                term = LoanTermType.SIX_MONTHS,
                amount = "1000000",
                currency = "CNY",
                interestArrangement = dtoInterestArrangementAdd,
                repaymentArrangement = dtoRepaymentArrangementAdd,
                feeArrangement = feeArrangement,
                borrower = 1,
                lender = lender
            )
        val actual = loanAgreementService.registered(dtoLoanAgreementAdd)

        assertThat(actual).isNotNull
    }
}