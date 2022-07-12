package cn.sunline.saas.document.model

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Document
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 15:57
 */
@Entity
@Table(
    name = "document", indexes = [
        Index(name = "idx_document_directory_id", columnList = "directory_id")
    ]
)
@EntityListeners(TenantListener::class)
class Document(
    @Id
    @Column(name = "document_id", nullable = false, columnDefinition = "bigint not null")
    val documentId: Long,

    @NotNull
    @Column(name = "document_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var documentName: String,

    @NotNull
    @Column(name = "document_version", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var documentVersion: String,

    @NotNull
    @Column(name = "document_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var documentType: DocumentType,

    @NotNull
    @Column(name = "directory_id", nullable = false, columnDefinition = "bigint not null")
    var directoryId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "document_status", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var documentStatus: DocumentStatus = DocumentStatus.NORMAL,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "document_format", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var documentFormat: DocumentFormat,

    @Temporal(TemporalType.TIMESTAMP)
    var creationDate: Date,

    @NotNull
    @Column(name = "document_location", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var documentLocation: String,

    @OneToMany(fetch = FetchType.EAGER, targetEntity = DocumentInvolvement::class, mappedBy = "documentId")
    @OrderBy("id ASC")
    var involvements: MutableList<DocumentInvolvement> = mutableListOf(),

    @NotNull
    @Column(name = "document_store_reference",nullable = false,length = 256,columnDefinition = "varchar(256) not null")
    var documentStoreReference: String,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

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