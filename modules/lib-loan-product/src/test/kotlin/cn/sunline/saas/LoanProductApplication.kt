package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @title: TenantApplication
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/1/29 14:22
 */
@SpringBootApplication
class LoanProductApplication

fun main(args: Array<String>) {
    runApplication<LoanProductApplication>(*args)
}
