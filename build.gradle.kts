plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "demo.ru"
version = "0.0.1-SNAPSHOT"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	dependencies {
		dependency("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
		dependency("org.springframework.boot:spring-boot-starter-security:6.3.4")
		dependency("org.springframework.boot:spring-boot-starter-web:3.3.4")
		dependency("org.springframework.boot:spring-boot-starter-validation:3.3.5")
		dependency("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")
		dependency("org.projectlombok:lombok:1.18.22")
		dependency("io.jsonwebtoken:jjwt-api:0.12.6")
		dependency("io.jsonwebtoken:jjwt-impl:0.12.6")
		dependency("io.jsonwebtoken:jjwt-jackson:0.12.6")
		dependency("org.springframework.boot:spring-boot-starter-test:3.3.4")
		dependency("org.springframework.security:spring-security-test:3.3.4")
		dependency("org.junit.platform:junit-platform-launcher:3.3.4")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
