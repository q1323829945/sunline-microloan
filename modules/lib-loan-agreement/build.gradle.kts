val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-global"))
    implementation(project(":lib-exceptions"))
    api(project(":lib-interest-arrangement"))
    api(project(":lib-fee-arrangement"))
    api(project(":lib-repayment-arrangement"))
    api(project(":lib-disbursement-arrangement"))
    api(project(":lib-invoice-arrangement"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))
}