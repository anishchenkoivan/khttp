plugins {
    kotlin("jvm") version "2.3.10"
    application
}

dependencies {
//    implementation(project(":khttp"))
    runtimeOnly("ch.qos.logback:logback-classic:1.5.19")
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
