package cn.sunline.saas.dapr_wrapper.actor.request

/**
 * @title: Timer
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/16 14:28
 */
data class ReminderRequest(
    val dueTime: String,
    val period: String = "",
    val data:Any? = null
)


data class Timer(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val milliseconds: Int = 0
) {
    override fun toString(): String {
        return "${hours}h${minutes}m${seconds}s${milliseconds}ms"
    }
}
