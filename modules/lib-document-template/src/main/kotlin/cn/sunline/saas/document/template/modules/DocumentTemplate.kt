package cn.sunline.saas.document.template.modules

import cn.sunline.saas.document.model.DocumentType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "document_template")
class DocumentTemplate(
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null,

        @NotNull
    @Column(name = "name", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var name:String,

        @NotNull
    @Column(name = "document_store_reference", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var documentStoreReference: String,

        @NotNull
    @Column(name = "bucket_name", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var bucketName: String,

    @Column(name = "file_type", nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var fileType: FileType,

        @NotNull
    @Column(name = "language_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var languageType: LanguageType,

        @NotNull
    @Column(name = "document_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var documentType: DocumentType,
)