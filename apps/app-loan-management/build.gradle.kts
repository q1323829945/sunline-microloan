val flywayVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val daprVersion: String by rootProject.extra
val hibernatetypeVersion: String by rootProject.extra
val log4jdbcVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")
    implementation("org.flywaydb:flyway-mysql:${flywayVersion}")
    implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("com.vladmihalcea:hibernate-types-55:${hibernatetypeVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("com.googlecode.log4jdbc:log4jdbc:${log4jdbcVersion}")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-response"))
    implementation(project(":lib-menu"))
    implementation(project(":lib-rbac"))
    implementation(project(":lib-multi-tenant"))
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
    api(project(":lib-template-data"))

    implementation(project(":lib-formula")) 
   implementation(project(":lib-apig-saas"))

    implementation("io.projectreactor:reactor-core:3.3.11.RELEASE")
}