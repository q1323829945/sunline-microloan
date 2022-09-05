package cn.sunline.saas.loan.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.loan.model.db.LoanAgent

interface LoanAgentRepository:BaseRepository<LoanAgent,Long> {
}