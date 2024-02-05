buildscript {

    dependencies {
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}


plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}
val storePassword by extra("android")
val keyPassword by extra("android")
