plugins {
    kotlin("jvm") version "2.1.20"
    id ("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")
}

tasks.test {
    useJUnit()
}
kotlin {
    jvmToolchain(23)
}