package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.repository.RepaymentScheduleDetailRepository
import org.springframework.stereotype.Service


@Service
class RepaymentScheduleDetailService(private val repaymentScheduleDetailRepository: RepaymentScheduleDetailRepository):
    BaseMultiTenantRepoService<RepaymentScheduleDetail, Long>(repaymentScheduleDetailRepository){

}
