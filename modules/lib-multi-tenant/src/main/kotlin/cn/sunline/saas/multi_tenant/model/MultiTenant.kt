package cn.sunline.saas.multi_tenant.model

/**
 * @title: MultiTenantInterface
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 11:31
 */
interface MultiTenant {

    fun getTenantId(): Long?

    fun setTenantId(o: Long)
}