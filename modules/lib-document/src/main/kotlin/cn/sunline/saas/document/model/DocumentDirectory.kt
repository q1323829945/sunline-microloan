package cn.sunline.saas.document.model

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
    @Column(name="directory_id",nullable = false, columnDefinition = "bigint not null")
    val directoryId: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var status: DocumentStatus = DocumentStatus.NORMAL,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @OrderBy("id ASC")
    var children: MutableList<DocumentDirectory> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "id")
    var parent: DocumentDirectory,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null
)