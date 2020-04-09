plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}
tasks.withType<Jar> {
    enabled = true
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.liquibase:liquibase-core:3.4.1")

    runtimeOnly("org.postgresql:postgresql:42.2.3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

val initPrimary by tasks.creating(org.springframework.boot.gradle.tasks.run.BootRun::class) {
    group = "Application"
    description = "Execute Replica Init ChangeLogRunner"
    main = "com.github.pcfleischer.eventbroker.commands.ChangeLogRunner"
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("spring.profiles.active", "local")
}

val initReplica by tasks.creating(org.springframework.boot.gradle.tasks.run.BootRun::class) {
    group = "Application"
    description = "Execute Replica Init ChangeLogRunner"
    main = "com.github.pcfleischer.eventbroker.commands.ChangeLogRunner"
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("spring.profiles.active", "local")
    args = listOf("--dataSource=replica")
}
