package cn.sunline.saas.risk.control.rule.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "risk_control_rule_param",
    indexes = [
        Index(name = "id_rule_id", columnList = "rule_id")
    ]
)
@EntityListeners(TenantListener::class)
class RiskControlRuleParam (
    @Id
    var id: Long? = null,

    @Column(name = "rule_id", columnDefinition = "bigint not null")
    var ruleId: Long? = null,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "data_item",  length = 128, columnDefinition = "varchar(128) not null")
    var dataItem: DataItem,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "relation_operator", length = 128, columnDefinition = "varchar(128) not null")
    var relationalOperatorType: RelationalOperatorType,

    @NotNull
    @Column(length = 128, columnDefinition = "varchar(128) not null")
    var threshold:String,

    @NotNull
    @Column(name = "logical_operation",length = 128, columnDefinition = "varchar(128) not null")
    var logicalOperationType: LogicalOperationType,
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


