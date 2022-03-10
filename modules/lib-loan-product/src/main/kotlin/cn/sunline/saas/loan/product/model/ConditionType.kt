package cn.sunline.saas.loan.product.model

/**
 * @title: ConditionMarker
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 17:08
 */
enum class ConditionType {
    AMOUNT {
        override fun getMarker(): String {
            return "amount"
        }
    },
    TERM {
        override fun getMarker(): String {
            return "term"
        }
    };

    abstract fun getMarker(): String
}