val jjwtVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val logVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")

    implementation(project(":lib-global"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-base-jpa"))
    implementation(project(":lib-interest"))
    implementation(project(":lib-repayment"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))
    //implementation(project(":lib-loan-product"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))
}
