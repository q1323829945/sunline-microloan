package cn.sunline.saas.multi_tenant.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.multi_tenant.model.Tenant
import java.util.UUID

/**
 * @title: TenantRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 12:05
 */
interface TenantRepository : BaseRepository<Tenant, Long>{
    fun findByUuid(uuid: UUID):Tenant?
}