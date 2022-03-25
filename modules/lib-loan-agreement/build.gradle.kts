val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-global"))
    implementation(project(":lib-interest-arrangement"))
    implementation(project(":lib-fee-arrangement"))
    implementation(project(":lib-repayment-arrangement"))
    api(project(":lib-base-jpa"))
    api(project(":lib-multi-tenant"))
    api(project(":lib-seq-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-snowflake"))
}