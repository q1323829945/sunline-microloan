package cn.sunline.saas.repayment.schedule.service


import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.repayment.schedule.repository.RepaymentScheduleRepository
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
class RepaymentScheduleService(private val repaymentScheduleRepository: RepaymentScheduleRepository):
    BaseMultiTenantRepoService<RepaymentSchedule, Long>(repaymentScheduleRepository){

    @Autowired
    private lateinit var seq: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Transactional
    fun register(dtoRepaymentScheduleAdd: DTORepaymentScheduleAdd): RepaymentSchedule {

        val repaymentScheduleDetail: MutableList<RepaymentScheduleDetail> = ArrayList()
        val repaymentScheduleId = seq.nextId()
        var totalInterest = BigDecimal.ZERO
        var totalRepayment = BigDecimal.ZERO
        for (schedule in dtoRepaymentScheduleAdd.schedule) {
            repaymentScheduleDetail += RepaymentScheduleDetail(
                id = seq.nextId(),
                repaymentScheduleId = repaymentScheduleId,
                period = schedule.period,
                installment = schedule.installment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = CalcDateComponent.parseViewToInstant(schedule.repaymentDate)
            )
            totalInterest += schedule.interest
            totalRepayment += (schedule.principal + schedule.interest)
        }
        val repaymentSchedule = RepaymentSchedule(
            id = repaymentScheduleId,
            installment = totalRepayment.subtract(totalInterest),
            term = dtoRepaymentScheduleAdd.term,
            interestRate = dtoRepaymentScheduleAdd.interestRate,
//            totalInterest = totalInterest,
//            totalRepayment = totalRepayment,
            schedule = repaymentScheduleDetail
        )
        return save(repaymentSchedule)
    }

    fun updateOne(oldRepaymentSchedule: RepaymentSchedule, newRepaymentSchedule: RepaymentSchedule): RepaymentSchedule {
//        oldRepaymentSchedule.totalRepayment = newRepaymentSchedule.totalRepayment
//        oldRepaymentSchedule.totalInterest = newRepaymentSchedule.totalInterest
        oldRepaymentSchedule.interestRate = newRepaymentSchedule.interestRate
        oldRepaymentSchedule.schedule = newRepaymentSchedule.schedule
        return save(oldRepaymentSchedule)
    }
}