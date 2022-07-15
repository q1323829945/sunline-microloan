package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdpaApplication {
    fun main(args: Array<String>) {
        runApplication<PdpaApplication>(*args)
    }
}

