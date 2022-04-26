val jacksonVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation(project(":lib-multi-tenant"))
    implementation(project(":lib-global"))
    implementation(project(":lib-seq-api"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-exceptions"))
    implementation("com.google.code.gson:gson:2.9.0")



    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-snowflake-simple"))
}