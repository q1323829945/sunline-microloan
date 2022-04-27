package cn.sunline.saas.risk.control.rule.modules.db

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
class RiskControlRuleParam (
    @Id
    val id: Long,

    @Column(name = "rule_id", columnDefinition = "bigint null")
    var ruleId: Long?,

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
)