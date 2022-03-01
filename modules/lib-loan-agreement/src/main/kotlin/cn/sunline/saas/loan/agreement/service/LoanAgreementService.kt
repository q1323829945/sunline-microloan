package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.loan.agreement.model.LoanAgreement
import cn.sunline.saas.loan.agreement.repository.LoanAgreementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: LoanAgreementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 11:21
 */
@Service
class LoanAgreementService(private val loanAgreementRepo: LoanAgreementRepository) :
    BaseMultiTenantRepoService<LoanAgreement, Long>(loanAgreementRepo)