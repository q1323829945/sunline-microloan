val jacksonVersion: String by rootProject.extra
val redissonVersion: String by rootProject.extra
val embeddedRedisVersion: String by rootProject.extra
val nettyVersion: String by rootProject.extra

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
	implementation("org.redisson:redisson:${redissonVersion}")
	implementation("io.netty:netty-all:${nettyVersion}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("it.ozimov:embedded-redis:${embeddedRedisVersion}")
}