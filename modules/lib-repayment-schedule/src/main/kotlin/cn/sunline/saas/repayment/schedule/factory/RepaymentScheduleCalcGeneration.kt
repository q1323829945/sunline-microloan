package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class RepaymentScheduleCalcGeneration {

    @Autowired
    private lateinit var calculatorFactory: CalculatorFactory

    fun calculator(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleTrialView {
        val calculator = calculatorFactory.instance(dtoRepaymentScheduleCalculateTrial.paymentMethod)
        return calculator.calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial)
    }

    fun calculatorReset(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule {
        val calculator = calculatorFactory.instance(dtoRepaymentScheduleResetCalculate.paymentMethod)
        return calculator.calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate)
    }
}
