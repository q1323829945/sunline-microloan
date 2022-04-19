val daprVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":lib-global"))
    api("io.dapr:dapr-sdk:${daprVersion}")
    implementation("com.google.code.gson:gson:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")

}