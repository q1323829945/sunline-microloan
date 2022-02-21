val flywayVersion: String by rootProject.extra
val mysqlConnectorVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val jodaVersion: String by rootProject.extra
val logVersion: String by rootProject.extra

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-activemq")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("io.github.microutils:kotlin-logging:${logVersion}")
	implementation("org.flywaydb:flyway-core:${flywayVersion}")
	implementation("org.flywaydb:flyway-mysql:${flywayVersion}")
	implementation("mysql:mysql-connector-java:${mysqlConnectorVersion}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
	implementation("joda-time:joda-time:${jodaVersion}")

	implementation(project(":lib-exceptions"))
	implementation(project(":lib-response"))
	implementation(project(":lib-redis"))
	implementation(project(":lib-mq"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}