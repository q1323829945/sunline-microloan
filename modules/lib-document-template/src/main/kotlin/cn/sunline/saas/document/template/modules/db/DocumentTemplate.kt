package cn.sunline.saas.document.template.modules.db

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.global.constant.LanguageType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "document_template",
    indexes = [
        Index(name = "idx_document_store_reference_unique", columnList = "document_store_reference", unique = true),
    ]


)
class DocumentTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotNull
    @Column(name = "name", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var name: String,

    @NotNull
    @Column(
        name = "document_store_reference",
        nullable = false,
        length = 256,
        columnDefinition = "varchar(256) not null"
    )
    var documentStoreReference: String,

    @NotNull
    @Column(name = "directory_id", nullable = false, columnDefinition = "bigint not null")
    var directoryId: Long,

    @Column(name = "file_type", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    @Enumerated(value = EnumType.STRING)
    var fileType: FileType,

    @NotNull
    @Column(name = "language_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    @Enumerated(value = EnumType.STRING)
    var languageType: LanguageType,

    @NotNull
    @Column(name = "document_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    @Enumerated(value = EnumType.STRING)
    var documentType: DocumentType,
)