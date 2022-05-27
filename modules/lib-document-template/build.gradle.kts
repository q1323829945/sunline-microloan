val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val embeddedRedisVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    api(project(":lib-base-jpa"))
    implementation(project(":lib-exceptions"))
    api(project(":lib-document"))
    implementation(project(":lib-obs-api"))
    implementation(project(":lib-seq-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("it.ozimov:embedded-redis:${embeddedRedisVersion}")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-seq-test"))
    testImplementation(project(":lib-obs-test"))
}