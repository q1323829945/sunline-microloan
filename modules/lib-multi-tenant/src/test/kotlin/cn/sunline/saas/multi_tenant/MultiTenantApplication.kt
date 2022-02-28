package cn.sunline.saas.multi_tenant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @title: TenantApplication
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/1/29 14:22
 */
@SpringBootApplication
class MultiTenantApplication

fun main(args: Array<String>) {
    runApplication<MultiTenantApplication>(*args)
}
