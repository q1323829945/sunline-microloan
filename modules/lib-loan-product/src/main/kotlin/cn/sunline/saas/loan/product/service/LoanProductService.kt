package cn.sunline.saas.loan.product.service

import cn.sunline.saas.interest.component.InterestProductFeatureComponent
import cn.sunline.saas.loan.product.component.LoanProductConditionComponent
import cn.sunline.saas.loan.product.model.db.LoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.repository.LoanProductRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
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
    BaseMultiTenantRepoService<LoanProduct, Long>(loanProductRepos) {

    @Autowired
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var loanProductCondition: LoanProductConditionComponent

    @Autowired
    private lateinit var interestProductFeatureService: InterestProductFeatureComponent

    fun register(loanProductData: DTOLoanProductAdd): LoanProduct {
        val newProductId = seq.nextId()
        val loanProduct = LoanProduct(
            newProductId,
            loanProductData.identificationCode,
            loanProductData.name,
            loanProductData.version,
            loanProductData.description,
            loanProductData.loanProductType,
            loanProductData.loanPurpose
        )

        loanProduct.configurationOptions =
            loanProductCondition.amountCondition(loanProductData.configuration.maxAmount, loanProductData.configuration.minAmount)
                .termCondition(loanProductData.configuration.maxTerm, loanProductData.configuration.minTerm).builder()


        loanProduct.interestFeature = interestProductFeatureService.register(
            newProductId,
            loanProductData.interestFeature
        )

        return loanProductRepos.save(loanProduct)

    }
}