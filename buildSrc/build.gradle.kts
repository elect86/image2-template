
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("net.idlestate:gradle-duplicate-classes-check:1.2.0")
}