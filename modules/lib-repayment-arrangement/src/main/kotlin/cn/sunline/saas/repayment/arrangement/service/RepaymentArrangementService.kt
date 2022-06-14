package cn.sunline.saas.repayment.arrangement.service

import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.arrangement.exception.RepaymentArrangementNotFoundException
import cn.sunline.saas.repayment.arrangement.factory.RepaymentArrangementFactory
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentAccount
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentArrangement
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentAccount
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import cn.sunline.saas.repayment.arrangement.repository.RepaymentArrangementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: RepaymentArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 14:43
 */
@Service
class RepaymentArrangementService(private val repaymentArrangementRepos: RepaymentArrangementRepository) :
    BaseMultiTenantRepoService<RepaymentArrangement, Long>(repaymentArrangementRepos) {

    @Autowired
    private lateinit var repaymentArrangementFactory: RepaymentArrangementFactory

    fun registered(agreementId: Long, dtoRepaymentArrangementAdd: DTORepaymentArrangementAdd): RepaymentArrangement {
        return save(repaymentArrangementFactory.instance(agreementId, dtoRepaymentArrangementAdd))
    }

    fun listRepaymentAccounts(agreementId: Long): MutableList<RepaymentAccount> {
        val repaymentArrangement = getOne(agreementId)
            ?: throw RepaymentArrangementNotFoundException("repayment arrangement not found")
        return repaymentArrangement.repaymentAccounts.filter { it.status == YesOrNo.Y }.toMutableList()
    }

    fun addRepaymentAccount(agreementId: Long, dtoRepaymentAccountList: MutableList<DTORepaymentAccount>): RepaymentArrangement{
        val repaymentArrangement = this.getOne(agreementId)?: throw RepaymentArrangementNotFoundException("repayment arrangement not found")
        return save(repaymentArrangementFactory.instanceRepaymentAccount(repaymentArrangement, dtoRepaymentAccountList))
    }
}