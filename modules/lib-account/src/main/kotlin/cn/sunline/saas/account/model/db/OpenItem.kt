package cn.sunline.saas.account.model.db

import cn.sunline.saas.account.model.OpenItemType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: OpenItem
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 11:43
 */
@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "open_item"
)
class OpenItem(
    @Id
    val id: Long,

    val openItemType: OpenItemType,

    val openItemReference: String

): MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}