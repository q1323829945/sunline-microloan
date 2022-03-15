package cn.sunline.saas.rule.engine.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
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
    name = "condition",
)
class Condition(

    @Id val id: Long,

    @NotNull @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    var type: String,

    @NotNull @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    val marker: String,

) {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

    @NotNull
    @Column(name = "max_value_range", nullable = false, columnDefinition = "bigint not null")
    private var maxValueRange: BigDecimal = BigDecimal.valueOf(Long.MAX_VALUE)

    @NotNull
    @Column(name = "min_value_range", nullable = false, columnDefinition = "bigint not null")
    private var minValueRange: BigDecimal = BigDecimal.valueOf(Long.MIN_VALUE)

    @JsonIgnore
    var description: String = ""

    fun setValue(maxValueRange: BigDecimal?, minValueRange: BigDecimal?) {
        if (maxValueRange != null) {
            this.maxValueRange = maxValueRange
        }
        if (minValueRange != null) {
            this.minValueRange = minValueRange
        }

        this.description = "${this.marker} > ${this.minValueRange} && ${this.marker} <= ${this.maxValueRange}"
    }

}