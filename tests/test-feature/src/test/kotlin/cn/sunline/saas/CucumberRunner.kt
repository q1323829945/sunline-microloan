package cn.sunline.saas

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty",
        "html:build/cucumber-report/html/cucumber.html",
        "json:build/cucumber-report/json/cucumber.json",
        "junit:build/cucumber-report/junit/cucumber.xml"],
    features = ["src/test/resources/cn/sunline/saas/cucumber"],
    glue = ["cn.sunline.saas.cucumber"],
    publish = true,
    monochrome = true,
)
class CucumberRunner {
}