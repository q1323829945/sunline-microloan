val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-global"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))
    implementation(project(":lib-channel-arrangement"))
    api(project(":lib-fee-util"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))
}