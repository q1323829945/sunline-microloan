package cn.sunline.saas.multi_tenant.jpa

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.exception.TenantUninitializedException
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

/**
 * @title: EntityListener
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 13:49
 */
@Component
class TenantListener<T : MultiTenant> {

    @PrePersist
    fun prePersist(o: T) {
        o.setTenantId(ContextUtil.getTenant().toLong())
    }
}