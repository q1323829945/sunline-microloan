import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.6.7" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10" apply false
	kotlin("plugin.spring") version "1.6.10" apply false
	kotlin("kapt") version "1.6.10" apply false
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
	repositories {
		mavenCentral()
	}
}

val jjwtVersion by extra { "0.9.1" }
val flywayVersion by extra { "8.5.2" }
val mysqlConnectorVersion by extra { "8.0.28" }
val jacksonVersion by extra { "2.13.2" }
val jodaVersion by extra { "2.10.13" }
val redissonVersion by extra { "3.16.8" }
val nettyVersion by extra { "4.1.75.Final" }
val embeddedRedisVersion by extra { "0.7.3" }
val logVersion by extra { "2.1.21" }
val hibernatetypeVersion by extra {"2.15.1"}
val log4jdbcVersion by extra {"1.2"}
val ktorVersion by extra {"2.0.1"}
val cucumberVersion by extra{"7.3.3"}
val junitJupiterVersion by extra{"5.8.2"}
val restAssuredVersion by extra{"5.1.1"}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
	apply(plugin = "org.jetbrains.kotlin.kapt")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	group = "cn.sunline"
	version = "0.0.1-SNAPSHOT"
	java.sourceCompatibility = JavaVersion.VERSION_17

	repositories {
		mavenCentral()
	}

	configurations {
		all {
			exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
		}
	}

	dependencies {
		implementation(kotlin("reflect"))
		implementation(kotlin("stdlib-jdk8"))
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1")
		implementation("io.github.microutils:kotlin-logging:2.1.21")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()

		val javaToolchains = project.extensions.getByType<JavaToolchainService>()
		javaLauncher.set(javaToolchains.launcherFor {
			languageVersion.set(JavaLanguageVersion.of(17))
		})
	}

	val project = project

	tasks.getByName<BootJar>("bootJar") {
		enabled = project.name.startsWith("app-")
	}

	tasks.getByName<Jar>("jar") {
		enabled = project.name.startsWith("lib-") || project.name.startsWith("test-")
	}
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.8"
}