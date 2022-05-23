package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.component.CalcPeriodComponent
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalcPeriodComponentTest {
    val detailsForOneMonth: MutableList<RepaymentScheduleDetail> = ArrayList()
    val detailsForThreeMonth: MutableList<RepaymentScheduleDetail> = ArrayList()
    val detailsForSixMonth: MutableList<RepaymentScheduleDetail> = ArrayList()
    val detailsForTwelveMonth: MutableList<RepaymentScheduleDetail> = ArrayList()
    init{
        val startDateTime = DateTime(2022, 3, 21, 0, 0)
        for(i in 0 until 12){
            detailsForOneMonth.add(
                RepaymentScheduleDetail(
                    id = 1,
                    repaymentScheduleId = 1111,
                    principal = BigDecimal(100),
                    interest = BigDecimal(20),
                    period = i + 1,
                    installment = BigDecimal(300),
                    repaymentDate = startDateTime.plusMonths(1).toInstant()
                )
            )
        }
    }

    @Test
    fun `calcDuePeriodsForOneMonth`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 21
        val repaymentFrequency = RepaymentFrequency.ONE_MONTH
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(12)
    }

    @Test
    fun `calcDuePeriodsForThreeMonth`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 21
        val repaymentFrequency = RepaymentFrequency.THREE_MONTHS
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(4)
    }

    @Test
    fun `calcDuePeriodsForSixMonth`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 21
        val repaymentFrequency = RepaymentFrequency.SIX_MONTHS
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(2)
    }

    @Test
    fun `calcDuePeriodsForTwelveMonth`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 21
        val repaymentFrequency = RepaymentFrequency.ONE_YEAR
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(1)
    }


    @Test
    fun `calcDuePeriodsForOneMonthAndAssignRepaymentDay`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 23
        val repaymentFrequency = RepaymentFrequency.ONE_YEAR
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(2)
    }

    @Test
    fun `calcDuePeriodsForThreeMonthAndAssignRepaymentDay`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 23
        val repaymentFrequency = RepaymentFrequency.ONE_YEAR
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(2)
    }

    @Test
    fun `calcDuePeriodsForSixMonthAndAssignRepaymentDay`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 23
        val repaymentFrequency = RepaymentFrequency.ONE_YEAR
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(2)
    }

    @Test
    fun `calcDuePeriodsForTwelveMonthAndAssignRepaymentDay`(){
        val startDateInstant = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDateInstant = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDay = 23
        val repaymentFrequency = RepaymentFrequency.ONE_YEAR
        val result = CalcPeriodComponent.calcDuePeriods(startDateInstant,endDateInstant,repaymentDay,repaymentFrequency)
        Assertions.assertThat(result).isEqualTo(2)
    }


    @Test
    fun `calcRemainPeriods`(){
        val startDateInstant = DateTime(2022, 7, 21, 0, 0).toInstant()
        val result = CalcPeriodComponent.calcRemainPeriods(startDateInstant,detailsForOneMonth)
        Assertions.assertThat(result).isEqualTo(arrayOf(0, 12, 0))
    }
}