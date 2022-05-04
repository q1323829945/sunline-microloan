val flywayVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val logVersion: String by rootProject.extra
val daprVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")
    implementation("org.flywaydb:flyway-mysql:${flywayVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-response"))
    implementation(project(":lib-global"))
    implementation(project(":lib-dapr-wrapper"))
    implementation(project(":lib-multi-tenant"))
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

}