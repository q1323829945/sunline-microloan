package cn.sunline.saas.global.constant

import org.joda.time.DateTime
import org.joda.time.Instant

/**
 * @title: TermType
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:10
 */
enum class LoanTermType(val days:Int) {
    ONE_MONTH(30) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusMonths(1).toInstant()
        }
    },
    THREE_MONTHS(90) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusMonths(2).toInstant()
        }
    },
    SIX_MONTHS(180) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusMonths(6).toInstant()
        }
    },
    ONE_YEAR(360) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusYears(1).toInstant()
        }
    },
    TWO_YEAR(720) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusYears(2).toInstant()
        }
    },
    THREE_YEAR(1080) {
        override fun calDays(startDay: Instant): Instant {
            return DateTime(startDay).plusYears(3).toInstant()
        }
    };

    abstract fun calDays(startDay: Instant): Instant
}