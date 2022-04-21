val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.google.code.gson:gson:2.9.0")



    implementation(project(":lib-exceptions"))
    implementation(project(":lib-seq-api"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(project(":lib-snowflake-simple"))
}