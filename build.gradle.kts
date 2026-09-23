// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
    alias(libs.plugins.google.gms.google.services) apply false

}
buildscript {
    dependencies {
        classpath ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("com.google.gms:google-services:4.4.2")
        val navVersion = "2.7.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
    repositories {
        google()
        maven { url = uri("https://www.jitpack.io") }
        maven {url = uri( "https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android")}
        mavenCentral()
    }
}




