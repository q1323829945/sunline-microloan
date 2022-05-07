package cn.sunline.saas.risk.control.rule.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "risk_control_rule",

    indexes = [
        Index(name = "idx_rule_type", columnList = "rule_type"),
        Index(name = "idx_rule_type", columnList = "sort"),
        Index(name = "idx_tenant_id", columnList = "tenant_id"),
    ]
)
@EntityListeners(TenantListener::class)
class RiskControlRule (
    @Id
    var id: Long? = null,

    @NotNull
    @Column( length = 128, columnDefinition = "varchar(128) not null")
    var name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "rule_type",length = 128, updatable = false, columnDefinition = "varchar(128) not null")
    var ruleType: RuleType,

    @NotNull
    @Column(columnDefinition = "bigint not null")
    var sort: Long?,

    @NotNull
    @Column(name = "logical_operation",length = 128, updatable = false, columnDefinition = "varchar(128) not null")
    var logicalOperationType: LogicalOperationType,

    @Column(length = 512, columnDefinition = "varchar(512)  null")
    var remark: String? = null,

    @Column(length = 512, columnDefinition = "varchar(512)  null")
    var description: String? = null,


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

) : MultiTenant {


    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH], orphanRemoval = true, mappedBy = "ruleId")
    var params:MutableList<RiskControlRuleParam> = mutableListOf()
    set(value) {
        this.params.clear()
        field.addAll(value)
    }

    @NotNull
    @Column(name = "tenant_id", columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}


