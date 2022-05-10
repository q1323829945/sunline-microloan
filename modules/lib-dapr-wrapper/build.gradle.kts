val daprVersion: String by rootProject.extra
val logVersion: String by rootProject.extra

dependencies {
    implementation(project(":lib-global"))
    api("io.dapr:dapr-sdk:${daprVersion}")
    implementation("io.github.microutils:kotlin-logging:${logVersion}")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")

}