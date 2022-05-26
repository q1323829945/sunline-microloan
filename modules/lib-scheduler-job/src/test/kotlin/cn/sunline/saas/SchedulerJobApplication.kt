package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @title: AccountApplication
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/1/29 14:22
 */
@SpringBootApplication
class SchedulerJobApplication

fun main(args: Array<String>) {
    runApplication<SchedulerJobApplication>(*args)
}
