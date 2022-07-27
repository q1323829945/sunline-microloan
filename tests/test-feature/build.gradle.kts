
val jacksonVersion: String by rootProject.extra
val cucumberVersion: String by rootProject.extra
val restAssuredVersion: String by rootProject.extra
val junitJupiterVersion: String by rootProject.extra


dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-core:${cucumberVersion}")
    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testImplementation("io.rest-assured:xml-path:${restAssuredVersion}")
    testImplementation("io.rest-assured:json-path:${restAssuredVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitJupiterVersion}")
}


tasks.withType<Test>{
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")

}

configurations{
    create("cucumberRuntime"){
        extendsFrom(testImplementation.get())
    }
}


tasks.create("cucumber"){
    dependsOn("assemble","testClasses")
    doLast {
        javaexec {
            mainClass.set("io.cucumber.core.cli.Main")
            classpath(configurations.getByName("cucumberRuntime") + sourceSets.main.get().output + sourceSets.test.get().output)
            args(
                "-p",
                "pretty",
                "-p",
                "html:build/cucumber-report/html/cucumber.html",
                "-p",
                "json:build/cucumber-report/json/cucumber.json",
                "-p",
                "junit:build/cucumber-report/junit/cucumber.xml",
                "-g",
                "cn.sunline.saas.test.steps",
                "-t",
                "not @Test"
            )
        }
    }
}