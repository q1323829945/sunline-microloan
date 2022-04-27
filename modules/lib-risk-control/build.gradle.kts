val jacksonVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-risk-control-rule"))
    implementation(project(":lib-rule-aviator"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-snowflake-simple"))
    testImplementation(project(":lib-global"))
}