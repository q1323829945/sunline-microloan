val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-obs-api"))
    implementation(project(":lib-document"))
    api(project(":lib-base-jpa"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}