package cn.sunline.saas.document.generation.models

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
    
    @NotNull
    @Column(name = "template_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var templateType:TemplateType,

)