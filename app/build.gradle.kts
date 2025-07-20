plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.maps_demos"
    compileSdk = 35

    buildFeatures {
        viewBinding =  true
    }


    defaultConfig {
        applicationId = "com.example.maps_demos"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}



dependencies {
    // Google Maps SDK for Android - displays interactive maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Kotlin BOM - ensures consistent versions of Kotlin libraries used across the app
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    // Glide - an efficient image loading and caching library for Android (commonly used for loading images from URLs, like place photos)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Android Maps Utils - provides utility functions for working with Google Maps on Android (e.g., PolyUtil for decoding polylines, clustering, etc.)
    implementation("com.google.maps.android:android-maps-utils:3.8.0")

    // Places SDK for Android - enables features like place autocomplete, fetching place details, and Search Along Route (SAR) requests
    implementation("com.google.android.libraries.places:places:4.4.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
