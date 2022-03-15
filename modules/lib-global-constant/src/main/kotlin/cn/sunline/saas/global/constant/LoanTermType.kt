package cn.sunline.saas.global.constant

import org.joda.time.DateTime
import java.util.*

/**
 * @title: TermType
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:10
 */
enum class LoanTermType {
    ONE_MONTH {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusMonths(1).toDate()
        }

        override fun convertDays(): Int {
            return 30
        }
    },
    THREE_MONTHS {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusMonths(2).toDate()
        }
        override fun convertDays(): Int {
            return 90
        }
    },
    SIX_MONTHS {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusMonths(6).toDate()
        }
        override fun convertDays(): Int {
            return 180
        }
    },
    ONE_YEAR {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusYears(1).toDate()
        }
        override fun convertDays(): Int {
            return 360
        }
    },
    TWO_YEAR {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusYears(2).toDate()
        }
        override fun convertDays(): Int {
            return 720
        }
    },
    THREE_YEAR {
        override fun calDays(startDay: Date): Date {
            return DateTime(startDay).plusYears(3).toDate()
        }
        override fun convertDays(): Int {
            return 1080
        }
    };

    abstract fun calDays(startDay: Date): Date
    abstract fun convertDays():Int
}