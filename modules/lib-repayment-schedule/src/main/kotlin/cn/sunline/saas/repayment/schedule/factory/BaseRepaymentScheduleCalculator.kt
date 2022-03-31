package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate

interface BaseRepaymentScheduleCalculator {

     fun calRepaymentSchedule(dtoRepaymentScheduleCalculate: DTORepaymentScheduleCalculate): RepaymentSchedule

     fun calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule
}
