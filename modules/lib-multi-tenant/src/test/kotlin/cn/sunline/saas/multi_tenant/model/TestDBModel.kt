package cn.sunline.saas.multi_tenant.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: TestDBModel
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 15:05
 */
@Entity
@EntityListeners(TenantListener::class)
class TestDBModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotNull
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    var uuid: UUID = UUID.randomUUID()

) : MultiTenant {

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