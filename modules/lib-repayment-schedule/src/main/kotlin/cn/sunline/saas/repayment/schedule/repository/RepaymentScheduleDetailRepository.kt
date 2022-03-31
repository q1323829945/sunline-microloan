package cn.sunline.saas.repayment.schedule.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail


interface RepaymentScheduleDetailRepository: BaseRepository<RepaymentScheduleDetail, Long> {
}