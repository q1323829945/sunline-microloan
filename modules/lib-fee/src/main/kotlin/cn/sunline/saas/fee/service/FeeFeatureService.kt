package cn.sunline.saas.fee.service

import cn.sunline.saas.fee.exception.FeeException
import cn.sunline.saas.fee.model.FeeMethodType
import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.fee.repository.FeeFeatureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

/**
 * @title: FeeFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:36
 */
@Service
class FeeFeatureService(private val feeFeatureRepo: FeeFeatureRepository) :
    BaseMultiTenantRepoService<FeeFeature, Long>(feeFeatureRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun register(productId: Long, dtoFeeFeatures: MutableList<DTOFeeFeatureAdd>): MutableList<FeeFeature> {

        val feeFeatures = mutableListOf<FeeFeature>()

        for (temp in dtoFeeFeatures) {
            if (temp.feeMethodType == FeeMethodType.FEE_RATIO && temp.feeRate == null) {
                throw FeeException("Fee calculation method config error")
            }
            if (temp.feeMethodType == FeeMethodType.FIX_AMOUNT && temp.feeAmount == null) {
                throw FeeException("Fee calculation method config error")
            }
            feeFeatures.add(
                FeeFeature(
                    seq.nextId(),
                    productId,
                    temp.feeType,
                    temp.feeMethodType,
                    temp.feeAmount,
                    temp.feeRate,
                    temp.feeDeductType
                )
            )
        }

        return save(feeFeatures).toMutableList()
    }


    fun findByProductId(productId:Long):MutableList<FeeFeature>{
        return feeFeatureRepo.findByProductId(productId)
    }
}