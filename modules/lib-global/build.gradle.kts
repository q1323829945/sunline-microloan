val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-snowflake"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}