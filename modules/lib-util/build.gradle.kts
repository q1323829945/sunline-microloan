val jjwtVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra

dependencies {
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(project(":lib-exceptions"))
    implementation("commons-io:commons-io:2.11.0")
    implementation(project(":lib-global"))
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}