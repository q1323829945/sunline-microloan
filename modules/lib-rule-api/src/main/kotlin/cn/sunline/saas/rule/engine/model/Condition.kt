package cn.sunline.saas.rule.engine.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Condition
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 15:45
 */
@Entity
@Table(
    name = "condition"
)
class Condition(

    @Id
    val id: Long? = null,

    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    @NotNull
    var name: String,

    @Column(nullable = false, length = 256, columnDefinition = "varchar(256) not null")
    var description: String = "",

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

)