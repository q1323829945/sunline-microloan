val jjwtVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-global"))
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-seq-api"))
    api(project(":lib-base-jpa"))


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("joda-time:joda-time:${jodaVersion}")
    testImplementation(project(":lib-seq-test"))
}