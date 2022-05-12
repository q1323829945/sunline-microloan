val flywayVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val logVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")
    implementation("org.flywaydb:flyway-mysql:${flywayVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-response"))
    implementation(project(":lib-redis"))
    implementation(project(":lib-dapr-wrapper"))
    implementation(project(":lib-global"))
    implementation(project(":lib-seq-api"))
    implementation(project(":lib-repayment-schedule"))
    implementation(project(":lib-loan-agreement"))
    implementation(project(":lib-banking-transaction"))
    implementation(project(":lib-repayment-schedule"))
    implementation(project(":lib-loan-product"))
    implementation(project(":lib-huaweicloud-obs"))
    implementation(project(":lib-document-generation"))
    implementation(project(":lib-document-template"))
    implementation(project(":lib-document"))
    implementation(project(":lib-party"))
    implementation(project(":lib-snowflake"))
    implementation(project(":lib-customer-offer"))
    implementation(project(":lib-account"))
    implementation(project(":lib-util"))
    implementation(project(":lib-pdpa"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}