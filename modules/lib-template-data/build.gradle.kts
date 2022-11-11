val jodaVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //添加Kotlin反射jar包
    implementation("org.jetbrains.kotlin:kotlin-reflect")


    implementation(project(":lib-exceptions"))
    implementation(project(":lib-global"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))

    implementation(project(":lib-interest"))
    implementation(project(":lib-document-template"))
    implementation(project(":lib-document"))
    implementation(project(":lib-loan-product"))
    implementation(project(":lib-repayment"))
    implementation(project(":lib-fee"))
    implementation(project(":lib-global"))
    implementation(project(":lib-interest-util"))
    implementation(project(":lib-seq-snowflake"))
    implementation(project(":lib-partner-integrated"))
    implementation(project(":lib-risk-control-rule"))
    implementation(project(":lib-pdpa"))
    implementation(project(":lib-risk-control"))
    implementation(project(":lib-obs-huaweicloud"))
    implementation(project(":lib-party"))
    implementation(project(":lib-dapr-wrapper"))
    implementation(project(":lib-customer-offer"))
    implementation(project(":lib-loan-agreement"))
    implementation(project(":lib-scheduler-job"))
    implementation(project(":lib-statistics"))
    implementation(project(":lib-filter"))
    implementation(project(":lib-schedule"))
    implementation(project(":lib-invoice"))
//    implementation(project(":lib-channel-interest"))
//    implementation(project(":lib-channel-product"))
//    implementation(project(":lib-channel-party"))
    api(project(":lib-template-data-util"))


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))
}