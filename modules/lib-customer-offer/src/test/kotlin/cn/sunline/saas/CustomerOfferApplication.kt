package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CustomerOfferApplication {
    fun main(args: Array<String>) {
        runApplication<CustomerOfferApplication>(*args)
    }
}

