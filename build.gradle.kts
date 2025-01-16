// Project-level build.gradle.kts

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
}

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.google.com")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // You can directly use the classpath for Kotlin plugin if you're not using version catalogs
        classpath(libs.kotlin.gradle.plugin.v200)
        classpath(libs.gradle)
    }
}



