package cn.sunline.saas.repayment.schedule.service


import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleAdd
import cn.sunline.saas.repayment.schedule.repository.RepaymentScheduleRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
class RepaymentScheduleService(private val repaymentScheduleRepository: RepaymentScheduleRepository):
    BaseMultiTenantRepoService<RepaymentSchedule, Long>(repaymentScheduleRepository){

    @Autowired
    private lateinit var seq: Sequence

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
            term = dtoRepaymentScheduleAdd.term!!,
            interestRate = dtoRepaymentScheduleAdd.interestRate,
            schedule = repaymentScheduleDetail
        )
        return save(repaymentSchedule)
    }

    fun updateOne(oldRepaymentSchedule: RepaymentSchedule, newRepaymentSchedule: RepaymentSchedule): RepaymentSchedule {
        oldRepaymentSchedule.interestRate = newRepaymentSchedule.interestRate
        oldRepaymentSchedule.schedule = newRepaymentSchedule.schedule
        return save(oldRepaymentSchedule)
    }
}