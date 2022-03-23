val jodaVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation(project(":lib-global"))
    api(project(":lib-base-jpa"))
    api(project(":lib-multi-tenant"))
    api(project(":lib-seq-api"))
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}