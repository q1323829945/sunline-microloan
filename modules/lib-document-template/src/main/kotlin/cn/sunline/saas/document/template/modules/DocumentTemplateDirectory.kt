package cn.sunline.saas.document.template.modules

import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "document_template_directory")
class DocumentTemplateDirectory(
    @Id
    var id: Long? = null,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var version: String = "1",

    @NotNull
    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) not null")
    var deleted: Boolean = false,

    @ManyToOne
    var parent: DocumentTemplateDirectory?,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "directory_id")
    var templates: MutableList<DocumentTemplate> = mutableListOf(),

    @Column(name = "directory_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    var directoryType: DirectoryType = DirectoryType.TEMPLATE,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "directory_id")
    var loanUploadConfigures: MutableList<LoanUploadConfigure> = mutableListOf()

) :  MultiTenant {

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