package cn.sunline.saas.abstract.core.agreement

import org.joda.time.DateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Arrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 15:25
 */
abstract class Arrangement(
    @Id
    var id: Long? = null,

    @NotNull @Column(name = "agreement_id",nullable = false,columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    val startDate: Date = DateTime.now().toDate(),

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var endDate: Date,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val status: ArrangementStatus

)