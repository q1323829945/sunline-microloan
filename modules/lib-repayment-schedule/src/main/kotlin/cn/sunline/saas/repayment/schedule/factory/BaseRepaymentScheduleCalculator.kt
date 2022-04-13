package cn.sunline.saas.repayment.schedule.factory

import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView

interface BaseRepaymentScheduleCalculator {

     fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleTrialView

     fun calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule
}
