package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @title: BankingTransactionApplication
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/1/29 14:22
 */
@SpringBootApplication
class BankingTransactionApplication

fun main(args: Array<String>) {
    runApplication<BankingTransactionApplication>(*args)
}
