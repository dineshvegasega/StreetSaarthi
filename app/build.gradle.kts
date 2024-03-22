@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.streetsaarthi.nasvi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.streetsaarthi.nasvi"
        minSdk = 26
        targetSdk = 34
        versionCode = 5
        versionName = "1.0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\admin\\AndroidStudioProjects\\StreetSaarthi\\nasvi.jks")
            storePassword = rootProject.extra["storePassword"] as String
            keyAlias = "nasvi"
            keyPassword = rootProject.extra["keyPassword"] as String
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
//        compose = true
        dataBinding = true
        viewBinding = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.10"
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    kapt {
        correctErrorTypes = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}


dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")

    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:hilt-android-compiler:2.49")


    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    //noinspection GradleCompatible,GradleCompatible
    implementation ("androidx.databinding:databinding-ktx:8.3.1")
    implementation ("androidx.databinding:databinding-runtime:8.3.1")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.squareup.picasso:picasso:2.8")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp ("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0") {
        exclude("glide-parent")
    }


    implementation ("androidx.preference:preference-ktx:1.2.1")
    implementation ("io.coil-kt:coil:2.4.0")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation ("id.zelory:compressor:3.0.1")
    implementation ("com.airbnb.android:lottie:6.3.0")

    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    debugImplementation ("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation ("com.google.android.play:core-ktx:1.8.1")
    implementation (platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-crashlytics-ktx")
    implementation ("com.google.firebase:firebase-config-ktx")

    implementation ("com.github.stfalcon-studio:StfalconImageViewer:v1.0.1")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.2")

    implementation ("androidx.paging:paging-common-ktx:3.2.1")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
//    implementation ("com.google.android.play:review-ktx:2.0.1")
}