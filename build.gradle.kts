import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

group = "io.github.smiley4"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.1.3"
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-auth:$ktorVersion")
    testImplementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    val jacksonVersion = "2.14.0"
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    val kotlinLoggingVersion = "2.1.23"
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

    val logbackVersion = "1.2.11"
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")

    val versionMockk = "1.12.7"
    testImplementation("io.mockk:mockk:$versionMockk")

    val versionKotest = "5.4.2"
    testImplementation("io.kotest:kotest-runner-junit5:$versionKotest")
    testImplementation("io.kotest:kotest-assertions-core:$versionKotest")

    val versionKotlinTest = "1.7.20"
    testImplementation("org.jetbrains.kotlin:kotlin-test:$versionKotlinTest")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "ktor-websockets-extended"
            from(components["java"])
            pom {
                name.set("Ktor WebSockets Extended")
                description.set("Ktor plugin with additional functionalities for (serverside) websockets")
                url.set("https://github.com/SMILEY4/ktor-websockets-extended")
            }
        }
    }
}