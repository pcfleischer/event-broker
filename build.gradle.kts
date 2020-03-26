import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE" apply false
	id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

allprojects {
    repositories {
        mavenCentral()
    }
	group = "com.github.pcfleischer"
	version = "0.0.1-SNAPSHOT"
	extra["springCloudVersion"] = "Hoxton.SR3"
}


/*
dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
*/