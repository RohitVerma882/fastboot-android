buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.navigation.safeArgs)
    }
}

plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}