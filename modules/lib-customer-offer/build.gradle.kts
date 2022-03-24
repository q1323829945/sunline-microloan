val jodaVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation(project(":lib-global"))
    api(project(":lib-base-jpa"))
    api(project(":lib-multi-tenant"))
    api(project(":lib-obs-api"))
    api(project(":lib-seq-api"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-snowflake"))
    implementation(project(":lib-huaweicloud"))
    implementation(project(":lib-pdpa"))
    implementation("com.google.code.gson:gson:2.9.0")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
}