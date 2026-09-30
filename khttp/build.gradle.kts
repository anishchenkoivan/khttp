plugins {
    kotlin("jvm") version "2.3.10"
    `java-library`
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
