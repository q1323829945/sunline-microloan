package cn.sunline.saas.underwriting.db

import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.util.Date
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Underwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:44
 */
@Entity
@Table(
    name = "underwriting",
)
@TypeDef(name = "json", typeClass = JsonStringType::class)
@EntityListeners(TenantListener::class)
class Underwriting(

    @Id
    val id: Long,

    @NotNull
    @Column(name = "customer_id", nullable = false, columnDefinition = "bigint not null")
    val customerId: Long,

    @NotNull
    @Type(type = "json")
    @Column(columnDefinition = "json not null")
    val applicationData: UnderwritingApplicationData,

    @Column(name = "customer_credit_rate",  length = 32, columnDefinition = "varchar(32) null")
    var customerCreditRate: String? = null,

    @Column(name = "credit_risk",  length = 32, columnDefinition = "varchar(32) null")
    var creditRisk: String? = null,

    @Column(name = "fraud_evaluation", length = 32, columnDefinition = "varchar(32) null")
    var fraudEvaluation: String? = null,

    @Column(name = "regulatory_compliance",  length = 32, columnDefinition = "varchar(32) null")
    var regulatoryCompliance: String? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 32, columnDefinition = "varchar(32) null")
    var status: UnderwritingType? = null,

    @CreationTimestamp
    var created: Date? = null,

    @UpdateTimestamp
    var updated: Date? = null
) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}