package cn.sunline.saas.partner.integrated.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.multi_tenant.context.TenantContext
import cn.sunline.saas.partner.integrated.model.db.PartnerIntegrated
import cn.sunline.saas.partner.integrated.repository.PartnerIntegratedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: UnderwritingService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 14:26
 */
@Service
class PartnerIntegratedService(private val partnerIntegratedRepo: PartnerIntegratedRepository) :
    BaseRepoService<PartnerIntegrated, Long>(partnerIntegratedRepo) {

    @Autowired
    private lateinit var tenantContext: TenantContext

    fun get(): PartnerIntegrated? {
        return getOne(tenantContext.get())
    }

    fun registered(partnerIntegrated: PartnerIntegrated): PartnerIntegrated {
        var poPartnerIntegrated = partnerIntegrated
        if (partnerIntegrated.tenantId == 0L) {
            poPartnerIntegrated = PartnerIntegrated(
                tenantContext.get(),
                partnerIntegrated.customerCreditRatingPartner,
                partnerIntegrated.creditRiskPartner,
                partnerIntegrated.regulatoryCompliancePartner,
                partnerIntegrated.fraudEvaluationPartner
            )
        }
        return save(poPartnerIntegrated)
    }

}