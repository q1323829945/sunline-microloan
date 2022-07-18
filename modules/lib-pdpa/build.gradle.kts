val jodaVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val hibernatetypeVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("joda-time:joda-time:${jodaVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("com.vladmihalcea:hibernate-types-55:${hibernatetypeVersion}")
    implementation(project(":lib-global"))
    implementation(project(":lib-exceptions"))
    implementation(project(":lib-obs-api"))
    implementation(project(":lib-seq-api"))
    implementation(project(":lib-multi-tenant"))


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("joda-time:joda-time:${jodaVersion}")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation(project(":lib-obs-test"))
    testImplementation(project(":lib-seq-test"))

}