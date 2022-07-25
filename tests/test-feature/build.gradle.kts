
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val cucumberVersion: String by rootProject.extra
val restAssuredVersion: String by rootProject.extra
val junitJupiterVersion: String by rootProject.extra

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-core:${cucumberVersion}")
    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testImplementation("io.rest-assured:xml-path:${restAssuredVersion}")
    testImplementation("io.rest-assured:json-path:${restAssuredVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${junitJupiterVersion}")



    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-global"))
    testImplementation(project(":lib-obs-test"))
    testImplementation(project(":lib-seq-test"))
    testImplementation(project(":lib-multi-tenant"))
}

