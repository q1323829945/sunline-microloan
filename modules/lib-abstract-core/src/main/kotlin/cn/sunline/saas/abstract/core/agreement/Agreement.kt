package cn.sunline.saas.abstract.core.agreement

import org.joda.time.DateTime
import java.util.Date
import java.util.concurrent.locks.Condition
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Agreement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 15:02
 */
abstract class Agreement<T:Arrangement>(

    @Id
    val id: Long? = null,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val agreementType: AgreementType,

    @NotNull
    @Temporal(TemporalType.DATE)
    val signedDate: Date = DateTime.now().toDate(),

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val fromDateTime: Date = DateTime.now().toDate(),

    @Temporal(TemporalType.TIMESTAMP)
    var toDateTime: Date? = null,

    @NotNull
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    var version: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val status: AgreementStatus,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "agreement_condition_mapping",
        joinColumns = [JoinColumn(name = "agreement_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "condition_id", referencedColumnName = "id")]
    )
    @OrderBy(value = "id ASC")
    var conditions: MutableList<Condition> = mutableListOf(),

    @OneToMany(fetch = FetchType.EAGER)
    var arrangements: MutableList<T> = mutableListOf()

)