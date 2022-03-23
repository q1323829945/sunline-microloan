val jodaVersion: String by rootProject.extra

dependencies {
    implementation("joda-time:joda-time:${jodaVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}