val daprVersion: String by rootProject.extra

dependencies {
    implementation(project(":lib-global"))
    api("io.dapr:dapr-sdk:${daprVersion}")
    api("io.dapr:dapr-sdk-springboot:${daprVersion}")
}