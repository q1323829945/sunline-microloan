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
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")
    implementation("org.flywaydb:flyway-mysql:${flywayVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("commons-httpclient:commons-httpclient:3.1")
    implementation("com.google.code.gson:gson:2.9.0")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-response"))
    implementation(project(":lib-redis"))
    implementation(project(":lib-util"))

    implementation(project(":lib-repayment"))
    implementation(project(":lib-fee"))
    implementation(project(":lib-global"))
    implementation(project(":lib-customer-offer"))
    implementation(project(":lib-loan-configure"))
//    implementation(project(":lib-huaweicloud-apig"))
    implementation(project(":lib-huaweicloud-obs"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}