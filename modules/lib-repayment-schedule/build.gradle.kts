









val jjwtVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val logVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation(project(":lib-global"))

    implementation(project(":lib-global-constant"))
    implementation("joda-time:joda-time:2.10.13")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation(project(":lib-exceptions"))
    api(project(":lib-base-jpa"))
    api(project(":lib-interest"))
    api(project(":lib-repayment"))
    api(project(":lib-multi-tenant"))
    api(project(":lib-seq-api"))
    implementation(project(":lib-snowflake"))
    api(project(":lib-loan-product"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-snowflake-simple"))
}
