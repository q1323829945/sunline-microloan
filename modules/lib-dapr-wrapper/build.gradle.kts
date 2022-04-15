val daprVersion: String by rootProject.extra

dependencies {
    implementation(project(":lib-global"))
//    api("io.dapr:dapr-sdk:${daprVersion}"){
//        exclude("com.squareup.okhttp3")
//    }
//    api("io.dapr:dapr-sdk-springboot:${daprVersion}"){
//        exclude("com.squareup.okhttp3")
//    }
//
    api("io.dapr:dapr-sdk:${daprVersion}")
//    api("io.dapr:dapr-sdk-springboot:${daprVersion}")
//
    api("com.squareup.okhttp3:okhttp:4.9.0")
}

//configurations.all {
//    resolutionStrategy {
//        force("com.squareup.okhttp3:okhttp:4.9.0")
//        force("com.squareup.okio:okio:2.8.0")
//    }
//}