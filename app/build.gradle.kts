plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.toulousehvl.myfoodtruck"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.toulousehvl.myfoodtruck"
        minSdk = 28
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val osmdroidVersion by extra( "6.1.18")
    val koinVersion  by extra("4.0.0-RC1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    // Koin
    implementation(libs.kotlinx.serialization.json)
    implementation("io.insert-koin:koin-core:$koinVersion")

    // Koin for Android
    implementation(libs.insert.koin.koin.android)
    // Koin AndroidX Scope
    implementation(libs.insert.koin.koin.androidx.scope)
    // Koin AndroidX ViewModel
    implementation(libs.insert.koin.koin.androidx.viewmodel)
    // Koin AndroidX Fragment
    implementation(libs.koin.androidx.fragment)
    // Koin AndroidX WorkManager
    implementation(libs.koin.androidx.workmanager)

    //Map
    implementation(libs.osmdroid.android)
    implementation(libs.androidx.activity)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}