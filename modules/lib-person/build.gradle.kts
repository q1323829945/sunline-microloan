val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-global"))
    implementation(project(":lib-multi-tenant"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}