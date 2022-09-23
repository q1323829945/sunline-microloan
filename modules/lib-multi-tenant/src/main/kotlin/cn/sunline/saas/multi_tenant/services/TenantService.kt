package cn.sunline.saas.multi_tenant.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.repository.TenantRepository
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * @title: TenantService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 10:22
 */
@Service
class TenantService(private val tenantRepository: TenantRepository) : BaseRepoService<Tenant, Long>(tenantRepository) {

    fun findBySaasUUID(saasUUID:UUID):Tenant?{
        return tenantRepository.findBySaasUUID(saasUUID)
    }

    fun findByUUID(uuid:UUID):Tenant?{
        return tenantRepository.findByUuid(uuid)
    }
}