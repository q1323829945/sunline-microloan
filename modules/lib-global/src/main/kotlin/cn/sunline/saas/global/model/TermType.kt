package cn.sunline.saas.global.model

import cn.sunline.saas.global.constant.TermUnit
import org.joda.time.DateTime
import org.joda.time.Instant

/**
 * @title: Term
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 16:20
 */
class TermType : Comparable<TermType> {
    val num: Int
    private val unit: TermUnit

    constructor(num: Int) {
        if (num % 12 == 0) {
            this.num = num / 12
            this.unit = TermUnit.YEAR
        } else {
            this.num = num
            this.unit = TermUnit.MONTH
        }
    }

    constructor(num: Int, unit: TermUnit) {
        this.num = num
        this.unit = unit
    }

    fun calDate(startDay: DateTime): DateTime {
        return when (unit) {
            TermUnit.MONTH -> startDay.plusMonths(num)
            TermUnit.YEAR -> startDay.plusYears(num)
        }
    }

    fun calBetweenMultiple(compareTerm: TermType): Int {
        val compare1 = this.toMonthUnit()
        val compare2 = compareTerm.toMonthUnit()
        return if (compare1.num < compare2.num) {
            1
        } else {
            compare1.num / compare2.num
        }
    }


    fun toMonthUnit(): TermType {
        return when (unit) {
            TermUnit.MONTH -> this
            TermUnit.YEAR -> TermType(num * 12, TermUnit.MONTH)
        }
    }

    override fun compareTo(other: TermType): Int {
        val c1 = this.toMonthUnit()
        val c2 = other.toMonthUnit()
        return if (c1.num < c2.num) {
            -1
        } else if (c1.num == c2.num) {
            0
        } else {
            1
        }
    }

    override fun equals(other: Any?): Boolean {
        return if(other is TermType ){
            this.num == other.num && this.unit == other.unit
        }else{
            false
        }
    }

    override fun toString(): String {
        return "num is $num, unit is $unit"
    }

}