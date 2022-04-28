package cn.sunline.saas.risk.control.rule.modules.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
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
class RiskControlRule (
    @Id
    val id: Long,

    @NotNull
    @Column( length = 128, columnDefinition = "varchar(128) not null")
    var name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "rule_type",length = 128, updatable = false, columnDefinition = "varchar(128) not null")
    var ruleType: RuleType,

    @NotNull
    @Column(columnDefinition = "bigint not null")
    var sort: Long,

    @Column(length = 512, columnDefinition = "varchar(512)  null")
    var remark: String? = null,

    @Column(nullable = false,length = 512, columnDefinition = "varchar(512)  null")
    var description: String? = null,


    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH])
    @JoinColumn(name = "rule_id")
    var params:MutableList<RiskControlRuleParam> = mutableListOf(),

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

) : MultiTenant {


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