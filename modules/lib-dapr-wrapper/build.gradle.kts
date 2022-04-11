val daprVersion: String by rootProject.extra

dependencies {
    api("io.dapr:dapr-sdk:${daprVersion}")
    api("io.dapr:dapr-sdk-springboot:${daprVersion}")
}