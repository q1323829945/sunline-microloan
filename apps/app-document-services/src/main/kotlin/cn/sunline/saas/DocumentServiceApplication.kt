package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentServiceApplication {
    fun main(args: Array<String>) {
        runApplication<DocumentServiceApplication>(*args)
    }
}