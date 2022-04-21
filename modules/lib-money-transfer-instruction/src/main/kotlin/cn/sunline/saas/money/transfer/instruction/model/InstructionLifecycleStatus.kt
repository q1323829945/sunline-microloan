package cn.sunline.saas.money.transfer.instruction.model

/**
 * @title: InstructionLifecycleStatus
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/19 16:48
 */
enum class InstructionLifecycleStatus {
    FAILED, FULFILLED, IN_PROGRESS, ON_HOLD, PREPARED, REQUEST
}