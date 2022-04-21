val jjwtVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")

    implementation(project(":lib-exceptions"))
    implementation("commons-io:commons-io:2.11.0")
    implementation(project(":lib-global"))
    implementation("commons-httpclient:commons-httpclient:3.1")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}