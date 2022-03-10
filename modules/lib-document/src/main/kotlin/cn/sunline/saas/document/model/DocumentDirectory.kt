package cn.sunline.saas.document.model

import cn.sunline.saas.abstract.core.directory.Directory
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: DocumentDirectory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 15:36
 */

@Entity
@Table(name = "document_directory")
class DocumentDirectory(
    @Id
    val id: Long? = null,
    name: String,
    version: String,
    deleted: Boolean = false,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @OrderBy("id ASC")
    var children: MutableList<DocumentDirectory> = mutableListOf(),

    @ManyToOne
    var parent: DocumentDirectory,


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
) : Directory(), MultiTenant {

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