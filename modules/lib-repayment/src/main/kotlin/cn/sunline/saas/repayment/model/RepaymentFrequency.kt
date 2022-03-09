package cn.sunline.saas.repayment.model

/**
 * @title: RepaymentFrequency
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 16:15
 */
enum class RepaymentFrequency {
    ONE_MONTH {
        override fun getMonths(): Int {
            return 1
        }
    },
    THREE_MONTHS {
        override fun getMonths(): Int {
            return 3
        }
    },
    SIX_MONTHS {
        override fun getMonths(): Int {
            return 6
        }
    },
    TWELVE_MONTHS {
        override fun getMonths(): Int {
            return 12
        }
    };

    abstract fun getMonths(): Int
}