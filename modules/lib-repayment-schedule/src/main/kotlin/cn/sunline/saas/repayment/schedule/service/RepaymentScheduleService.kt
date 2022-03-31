package cn.sunline.saas.repayment.schedule.service


import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.repository.RepaymentScheduleRepository
import org.springframework.stereotype.Service

@Service
class RepaymentScheduleService(private val repaymentScheduleRepository: RepaymentScheduleRepository):
    BaseMultiTenantRepoService<RepaymentSchedule, Long>(repaymentScheduleRepository){


}