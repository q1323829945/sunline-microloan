package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestFeatureApplication {
    fun main(args: Array<String>) {
        runApplication<TestFeatureApplication>(*args)
    }
}
