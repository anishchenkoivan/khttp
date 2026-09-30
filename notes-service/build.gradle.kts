plugins {
    kotlin("jvm") version "2.3.10"
    application
}

dependencies {
    implementation(project(":khttp"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "ru.notes.MainKt"
}
