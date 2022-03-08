val jjwtVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation("commons-httpclient:commons-httpclient:3.1")

    implementation("joda-time:joda-time:2.10.13")



    implementation(project(":lib-exceptions"))
    implementation(project(":lib-redis"))
    implementation(project(":lib-obs-api"))

    api(project(":lib-base-jpa"))


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}