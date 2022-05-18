package cn.sunline.saas.dapr_wrapper.actor.request

/**
 * @title: Timer
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/16 14:28
 */
data class ReminderRequest(
    val dueTime: String,
    val period: String = ""
)


data class Timer(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val milliseconds: Int = 0
) {
    override fun toString(): String {
        return "${hours.toString()}h${minutes.toString()}m${seconds.toString()}s${milliseconds.toString()}ms"
    }
}
