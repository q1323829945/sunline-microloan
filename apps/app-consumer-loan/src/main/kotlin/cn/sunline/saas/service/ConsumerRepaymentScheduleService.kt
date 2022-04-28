package cn.sunline.saas.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleAdd
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.http.ResponseEntity
import java.math.BigDecimal

interface ConsumerRepaymentScheduleService {

    fun initiate()

    fun calculate(productId: Long,amount: BigDecimal,term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>>

    fun register(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>>

    fun updateOne(id: Long, productId: Long, repaymentDate: String, term: LoanTermType, remainLoanAmount: BigDecimal)
}