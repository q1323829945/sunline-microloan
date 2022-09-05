package cn.sunline.saas.channel.product.model.db

import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.channel.product.model.QuestionnaireAnswerType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "questionnaire",
)
@EntityListeners(TenantListener::class)
class Questionnaire(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "question", nullable = false, columnDefinition = "text")
    var question: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "answer_type", nullable = false, columnDefinition = "text")
    var answerType: QuestionnaireAnswerType,

)  : MultiTenant {
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
