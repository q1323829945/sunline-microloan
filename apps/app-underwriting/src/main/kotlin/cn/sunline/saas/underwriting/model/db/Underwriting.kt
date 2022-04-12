package cn.sunline.saas.underwriting.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
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

    @Column(name = "customer_credit_rate", nullable = false, length = 32, columnDefinition = "varchar(32) null")
    var customerCreditRate: String? = null,

    @Column(name = "credit_risk", nullable = false, length = 32, columnDefinition = "varchar(32) null")
    var creditRisk: String? = null,

    @Column(name = "fraud_evaluation", nullable = false, length = 32, columnDefinition = "varchar(32) null")
    var fraudEvaluation: String? = null,

    @Column(name = "regulatory_compliance", nullable = false, length = 32, columnDefinition = "varchar(32) null")
    var regulatoryCompliance: String? = null,

    @CreationTimestamp
    var created: Instant? = null,

    @UpdateTimestamp
    var updated: Instant? = null
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