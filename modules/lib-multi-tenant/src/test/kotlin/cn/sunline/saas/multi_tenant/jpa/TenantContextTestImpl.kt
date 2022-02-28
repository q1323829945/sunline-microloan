package cn.sunline.saas.multi_tenant.jpa

import cn.sunline.saas.multi_tenant.context.TenantContext
import org.springframework.stereotype.Component

/**
 * @title: TenantContextTestImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 15:22
 */
@Component
class TenantContextTestImpl : TenantContext {
    override fun get(): Long? {
        return 100
    }
}