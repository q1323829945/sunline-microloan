package cn.sunline.saas.scheduler

import cn.sunline.saas.dapr_wrapper.actor.request.Timer

enum class ActorType(var dueTime: Timer?,var periodTime:Timer?) {
    LOAN_APPLY_HANDLE(Timer(),Timer(seconds = 50)),
    LOAN_APPLY_SUBMIT(Timer(seconds = 30),Timer(seconds = 30)),
    LOAN_APPLY_STATISTICS(Timer(seconds = 30),Timer(seconds = 30)),
    SYNC_CHANNEL(Timer(seconds = 30),Timer(seconds = 30)),
    CHANNEL_STATISTICS(Timer(seconds = 30),Timer(seconds = 30)),
    BUSINESS_STATISTICS(Timer(seconds = 30),Timer(seconds = 30))
}