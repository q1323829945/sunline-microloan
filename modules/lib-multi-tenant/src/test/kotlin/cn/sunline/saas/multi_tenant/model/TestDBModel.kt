package cn.sunline.saas.multi_tenant.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import org.joda.time.DateTime
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
@Table(name="test_db_model")
class TestDBModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotNull
    @Column(columnDefinition = "varchar(100)", nullable = false, updatable = false)
    var uuid: String = "UUID.randomUUID()",

    @Column(nullable = false, updatable = false,columnDefinition = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    var date:Date

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