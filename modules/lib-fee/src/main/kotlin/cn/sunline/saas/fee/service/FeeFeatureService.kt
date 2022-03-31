package cn.sunline.saas.fee.service

import cn.sunline.saas.fee.model.db.FeeFeature
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.fee.repository.FeeFeatureRepository
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

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

        dtoFeeFeatures.forEach {
            val feeAmount = it.feeAmount?.run {
                BigDecimal(this)
            }

            val feeRate = it.feeRate?.run {
                BigDecimal(this)
            }

            FeeUtil.validFeeConfig(it.feeMethodType, feeAmount, feeRate)

            feeFeatures.add(
                FeeFeature(
                    seq.nextId(),
                    productId,
                    it.feeType,
                    it.feeMethodType,
                    feeAmount,
                    feeRate,
                    it.feeDeductType
                )
            )
        }

        return save(feeFeatures).toMutableList()
    }


    fun findByProductId(productId:Long):MutableList<FeeFeature>{
        return feeFeatureRepo.findByProductId(productId)
    }
}