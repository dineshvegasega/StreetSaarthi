plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
//    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.streetsaarthi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.streetsaarthi"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
//            excludes.add("META-INF/LICENSE.md")
//            excludes.add("META-INF/LICENSE-notice.md")
//
//            excludes.add("META-INF/DEPENDENCIES")
//            excludes.add("META-INF/LICENSE")
//            excludes.add("META-INF/LICENSE.txt")
//            excludes.add("META-INF/license.txt")
//            excludes.add("META-INF/NOTICE")
//            excludes.add("META-INF/NOTICE.txt")
//            excludes.add("META-INF/notice.txt")
//            excludes.add("META-INF/*.kotlin_module")
//            excludes.add("META-INF/LICENSE-notice.md")
        }
    }



    kapt {
        correctErrorTypes = true
    }


}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.1")
    //noinspection GradleCompatible,GradleCompatible
    implementation ("androidx.databinding:databinding-ktx:8.1.1")
    implementation ("androidx.databinding:databinding-runtime:8.1.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


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


    implementation ("androidx.preference:preference-ktx:1.2.1")
    implementation ("io.michaelrocks:libphonenumber-android:8.12.52")
//    implementation ("com.github.ChiliLabs:ChiliPhotoPicker:0.3.1")
    implementation ("io.coil-kt:coil:2.2.1")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation ("id.zelory:compressor:3.0.1")

//    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
//    implementation ("com.google.firebase:firebase-auth-ktx")
//    implementation ("com.google.firebase:firebase-database-ktx")
////    implementation ("com.google.firebase:firebase-messaging-ktx")
//    implementation ("com.google.firebase:firebase-analytics-ktx")
//    implementation ("com.google.firebase:firebase-crashlytics-ktx")
//    implementation ("com.google.firebase:firebase-config-ktx")


//    implementation ("com.google.firebase:firebase-ml-natural-language:22.0.1")
//    implementation ("com.google.firebase:firebase-ml-natural-language-language-id-model:20.0.8")
//    implementation ("com.google.firebase:firebase-ml-natural-language-translate-model:20.0.9")


//    implementation ("com.google.firebase:firebase-ml-natural-language:22.0.1")
//    implementation ("com.google.firebase:firebase-ml-natural-language-translate-model:20.0.9")
//

////    implementation ("com.google.guava:guava:27.0.1-jre")
//    implementation("com.google.cloud:google-cloud-translate:1.12.0"){
//        configurations.all {
////            exclude("org.apache.http components")
////            exclude("org.json', module: 'json")
////            exclude("com.google.api-client")
//        }
//    }
//
//
//    annotationProcessor ("com.google.cloud:google-cloud-translate:1.12.0")

//    implementation ("com.google.api-client:google-api-client:1.33.0")


//    implementation ("com.google.api-client:google-api-client:1.33.0"){
//        configurations.all {
//            exclude("org.apache.http components")
////            exclude("org.json', module: 'json")
//            exclude("com.google.api-client")
//
//        }
//    }


}