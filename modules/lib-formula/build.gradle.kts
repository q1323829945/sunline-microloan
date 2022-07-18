val jodaVersion: String by rootProject.extra

dependencies {
    implementation("joda-time:joda-time:${jodaVersion}")

    implementation(project(":lib-global"))

    implementation(project(":lib-fee-util"))


    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
