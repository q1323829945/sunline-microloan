val jacksonVersion: String by rootProject.extra
val logVersion: String by rootProject.extra
val ktorVersion:String by rootProject.extra

dependencies {
    implementation("io.github.microutils:kotlin-logging:${logVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation(project(":lib-global"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-response"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}