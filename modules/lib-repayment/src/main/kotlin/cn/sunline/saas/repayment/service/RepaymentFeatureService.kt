package cn.sunline.saas.repayment.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.factory.RepaymentFeatureFactory
import cn.sunline.saas.repayment.model.db.RepaymentFeature
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import cn.sunline.saas.repayment.repository.RepaymentFeatureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: RepaymentProductFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/11 14:33
 */
@Service
class RepaymentFeatureService(private val repaymentFeatureRepo: RepaymentFeatureRepository) :
    BaseMultiTenantRepoService<RepaymentFeature, Long>(repaymentFeatureRepo) {
    @Autowired
    private lateinit var repaymentFeatureFactory: RepaymentFeatureFactory

    fun register(productId: Long, repaymentFeatureData: DTORepaymentFeatureAdd): RepaymentFeature {
        return save(repaymentFeatureFactory.instance(productId, repaymentFeatureData))
    }

    fun findByProductId(productId:Long):RepaymentFeature?{
        return repaymentFeatureRepo.findByProductId(productId)
    }
}