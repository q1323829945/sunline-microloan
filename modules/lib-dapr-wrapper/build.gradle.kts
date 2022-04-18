val daprVersion: String by rootProject.extra

dependencies {
    implementation(project(":lib-global"))
    api("io.dapr:dapr-sdk:${daprVersion}")
    api("io.dapr:dapr-sdk-springboot:${daprVersion}")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")
}