package cn.sunline.saas.loan.service

import cn.sunline.saas.loan.dto.DTOLoanApplication
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleAdd
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
interface ConsumerRepaymentScheduleService {

    fun initiate()

    fun calculate(productId: Long,amount: BigDecimal,term: Int): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>>

    fun save(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>>
}