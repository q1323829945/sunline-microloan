package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoanProductApplication

fun main(args: Array<String>) {
    runApplication<LoanProductApplication>(*args)
}
