package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class RepaymentScheduleCalcGeneration {

    @Autowired
    private lateinit var calculatorFactory: CalculatorFactory

    fun calculator(dtoRepaymentScheduleCalculate: DTORepaymentScheduleCalculate): RepaymentSchedule {
        val calculator = calculatorFactory.instance(dtoRepaymentScheduleCalculate.paymentMethod)
        return calculator.calRepaymentSchedule(dtoRepaymentScheduleCalculate)
    }

    fun calculatorReset(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule {
        val calculator = calculatorFactory.instance(dtoRepaymentScheduleResetCalculate.paymentMethod)
        return calculator.calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate)
    }
}
