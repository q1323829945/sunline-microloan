package cn.sunline.saas.multi_tenant.jpa

import cn.sunline.saas.multi_tenant.context.TenantContext
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

/**
 * @title: EntityListener
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 13:49
 */
@Component
class TenantListener<T : MultiTenant>(
    @Autowired private val context: TenantContext
) {

    @PrePersist
    fun prePersist(o: T) {
        o.setTenantId(context.get())
    }
}