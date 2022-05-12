val jjwtVersion: String by rootProject.extra
val embeddedRedisVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation(project(":lib-exceptions"))
    api(project(":lib-seq-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}