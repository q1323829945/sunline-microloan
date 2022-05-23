package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.disbursement.arrangement.model.DisbursementLendType
import cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestRate
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTOPrepaymentArrangementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentAccount
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
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

    @BeforeAll
    fun `init tenant`(){
        ContextUtil.setTenant("123456678")
    }

    @Test
    fun `loan agreement save`() {
        val planRates = mutableListOf<DTOInterestRate>()
        val rate1 = DTOInterestRate(id = 1, LoanTermType.SIX_MONTHS, rate = "7.2")
        val rate2 = DTOInterestRate(id = 2, LoanTermType.ONE_YEAR, rate = "12.5")
        planRates.add(rate1)
        planRates.add(rate2)
        val dtoInterestArrangementAdd = DTOInterestArrangementAdd(
            InterestType.FIXED, BaseYearDays.ACTUAL_YEAR, "1",
            "150", planRates
        )

        val prepaymentArrangement = mutableListOf<DTOPrepaymentArrangementAdd>()
        val dtoPrepaymentArrangementAdd1 =
            DTOPrepaymentArrangementAdd(LoanTermType.ONE_MONTH, PrepaymentType.NOT_ALLOWED, "150")
        prepaymentArrangement.add(dtoPrepaymentArrangementAdd1)

        val dtoRepaymentAccount = mutableListOf<DTORepaymentAccount>()
        dtoRepaymentAccount.add(DTORepaymentAccount("123455", "120"))
        dtoRepaymentAccount.add(DTORepaymentAccount("1234555566666", "1201111"))
        val dtoRepaymentArrangementAdd = DTORepaymentArrangementAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.SIX_MONTHS, RepaymentDayType.BASE_LOAN_DAY, prepaymentArrangement, dtoRepaymentAccount
        )
        val feeArrangement = mutableListOf<DTOFeeArrangementAdd>()
        val dtoFeeArrangementAdd = DTOFeeArrangementAdd(
            "test", FeeMethodType.FIX_AMOUNT, BigDecimal("100"), null,
            FeeDeductType.IMMEDIATE
        )
        feeArrangement.add(dtoFeeArrangementAdd)

        val lender = mutableListOf<Long>()
        lender.add(2)

        val disbursementArrangement = DTODisbursementArrangementAdd("123455", "120", DisbursementLendType.ONCE)

        val dtoLoanAgreementAdd =
            DTOLoanAgreementAdd(
                productId = 1,
                term = LoanTermType.SIX_MONTHS,
                amount = "1000000",
                currency = CurrencyType.USD,
                interestArrangement = dtoInterestArrangementAdd,
                repaymentArrangement = dtoRepaymentArrangementAdd,
                feeArrangement = feeArrangement,
                borrower = 1,
                lender = lender,
                disbursementArrangement = disbursementArrangement,
                applicationId = 1,
                userId = 1,
                purpose = null,
                graceDays = 0
            )
        val actual = loanAgreementService.registered(dtoLoanAgreementAdd)

        assertThat(actual).isNotNull
    }
}