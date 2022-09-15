package cn.sunline.saas.pdpa.modules.db

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "pdpa",
    indexes = [
        Index(name = "idx_country_tenant", columnList = "country,language,tenant_id",unique = true),
    ]
)
@TypeDef(name = "json", typeClass = JsonStringType::class)
@EntityListeners(TenantListener::class)
class Pdpa (
    @Id
    val id: Long,

    @Column(updatable = false, length = 32, columnDefinition = "varchar(32) not null")
    @NotNull
    @Enumerated(value = EnumType.STRING)
    val country:CountryType,

    @Column(updatable = false, length = 32, columnDefinition = "varchar(32) not null")
    @NotNull
    val language:String,

    @Column(name = "information_file_path", length = 1024, columnDefinition = "varchar(1024) not null")
    @NotNull
    var informationFilePath:String,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id",columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }

}