import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        //plugin needed to generate POJO from Avro schema
        classpath("com.commercehub.gradle.plugin:gradle-avro-plugin:0.14.2")
    }
}

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven { url = uri("http://packages.confluent.io/maven/") }
    }
    group = "com.github.pcfleischer"
    version = "0.0.1-SNAPSHOT"
    extra["springCloudVersion"] = "Hoxton.SR3"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}