package cn.sunline.saas.document.model

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: DocumentInvolvement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/22 10:49
 */
@Entity
@Table(
    name = "document_involvement", indexes = [
        Index(name = "idx_document_involvement_document_and_party_id", columnList = "document_id,party_id", unique = true),
    ]
)
class DocumentInvolvement(

    @Id
    val id: Long,

    @NotNull
    @Column(name = "document_involvement_type", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val documentInvolvementType: DocumentInvolvementType,

    @NotNull
    @Column(name = "party_id", nullable = false, columnDefinition = "bigint not null")
    val partyId:Long,

    @NotNull
    @Column(name = "document_id", nullable = false, columnDefinition = "bigint not null")
    val documentId:Long
)