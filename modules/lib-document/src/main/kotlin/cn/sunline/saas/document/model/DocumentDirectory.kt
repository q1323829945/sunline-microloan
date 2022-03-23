package cn.sunline.saas.document.model

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
    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var version: String,

    @NotNull
    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) not null")
    var deleted: Boolean = false,

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