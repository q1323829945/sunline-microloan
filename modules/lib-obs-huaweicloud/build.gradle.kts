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
    implementation(project(":lib-redis"))
    implementation(project(":lib-global"))
    implementation(project(":lib-util"))
    implementation(project(":lib-obs-api"))
    implementation(project(":lib-seq-api"))
    api("com.squareup.okhttp3:okhttp:4.9.0")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))


}