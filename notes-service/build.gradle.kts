plugins {
    kotlin("jvm") version "2.3.10"
    application
}

dependencies {
    implementation(project(":khttp"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.22.3")
    runtimeOnly("ch.qos.logback:logback-classic:1.6.5")
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
