val jodaVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation(project(":lib-global"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))
    implementation(project(":lib-rule-api"))
    implementation(project(":lib-exceptions"))
    api(project(":lib-interest"))
    api(project(":lib-repayment"))
    api(project(":lib-fee"))
    implementation(project(":lib-fee-util"))
    implementation(project(":lib-interest-util"))
    implementation(project(":lib-document-template"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-snowflake-simple"))
}