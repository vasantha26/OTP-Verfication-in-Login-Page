buildscript {
    repositories {
        google()
        mavenCentral()
    }


    dependencies {

        classpath(libs.google.services.v441)
    }
}

plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}