package cn.sunline.saas.loan.product.service

import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.interest.model.InterestProductFeature
import cn.sunline.saas.interest.model.InterestProductFeatureModality
import cn.sunline.saas.interest.model.InterestType
import cn.sunline.saas.interest.service.InterestProductFeatureService
import cn.sunline.saas.loan.product.model.LoanProduct
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.loan.product.repository.LoanProductRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * @title: LoanProductService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:07
 */
@Service
class LoanProductService(private val loanProductRepos: LoanProductRepository) :
    BaseMultiTenantRepoService<LoanProduct<ProductFeature>, Long>(loanProductRepos) {

    @Autowired
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var interestProductFeatureService: InterestProductFeatureService

    fun register(interestType: InterestType, ratePlanId: Long, frequency: String, calculationMethod: String) {
        val newProductId = seq.nextId()
        var productFeatures: MutableList<ProductFeature> = mutableListOf()

        val interestFeature = interestProductFeatureService.register(newProductId,interestType, ratePlanId, frequency, calculationMethod)
        productFeatures.add(interestFeature)

        
    }
}