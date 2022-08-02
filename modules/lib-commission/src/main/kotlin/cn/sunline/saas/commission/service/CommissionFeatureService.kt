package cn.sunline.saas.commission.service

import cn.sunline.saas.commission.model.db.CommissionFeature
import cn.sunline.saas.commission.model.dto.DTOCommissionFeatureAdd
import cn.sunline.saas.commission.repository.CommissionFeatureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class CommissionFeatureService(private val commissionFeatureRepo: CommissionFeatureRepository) :
    BaseMultiTenantRepoService<CommissionFeature, Long>(commissionFeatureRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun register(channel: String, dtoCommissionFeatureAdd: MutableList<DTOCommissionFeatureAdd>): MutableList<CommissionFeature> {

        val commissionFeatures = mutableListOf<CommissionFeature>()

        dtoCommissionFeatureAdd.forEach {
            commissionFeatures.add(
                CommissionFeature(
                    id = seq.nextId(),
                    channel = channel,
                    commissionType = it.commissionType,
                    commissionMethodType = it.commissionMethodType,
                    commissionAmount = it.commissionAmount,
                    commissionRatio = it.commissionRatio
                )
            )
        }

        return save(commissionFeatures).toMutableList()
    }
}