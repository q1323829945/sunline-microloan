package cn.sunline.saas.interest.model

/**
 * @title: BaseYearDays
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 16:43
 */
enum class BaseYearDays {
    ACCOUNT_YEAR {
        override fun getDays(): Int {
            return 360
        }
    },
    ACTUAL_YEAR {
        override fun getDays(): Int {
            return 365
        }
    };

    abstract fun getDays(): Int
}