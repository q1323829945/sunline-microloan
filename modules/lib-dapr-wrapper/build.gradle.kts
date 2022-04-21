val daprVersion: String by rootProject.extra

dependencies {
    implementation(project(":lib-global"))
    api("io.dapr:dapr-sdk:${daprVersion}")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")

}