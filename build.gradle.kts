plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "com.caseykulm.santasolver"
version = "0.1"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.choco-solver:choco-solver:4.10.6")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")

    testImplementation(kotlin("test"))
}