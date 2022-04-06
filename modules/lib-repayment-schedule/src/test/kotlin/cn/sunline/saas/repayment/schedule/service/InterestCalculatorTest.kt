package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.repayment.schedule.component.CalcInterestComponent
import cn.sunline.saas.repayment.schedule.model.dto.DTOInterestCalculator
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterestCalculatorTest {

    @Test
    fun `calcBaseInterest`(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcInterestComponent.calcBaseInterest(
            DTOInterestCalculator(
                calcAmount = BigDecimal(10000),
                loanRateMonth = BigDecimal(0.12),
                loanRateDay = BigDecimal(0.01),
                currentRepaymentDateTime = startDate,
                nextRepaymentDateTime = endDate
            )
        )
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }


    @Test
    fun `calcDayInterest`(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcInterestComponent.calcDayInterest(
            BigDecimal(10000),
            BigDecimal(0.01),
            startDate,
            endDate
        )
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }

}