plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.dagger)
    kotlin("kapt")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.toulousehvl.myfoodtruck"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.toulousehvl.myfoodtruck"
        minSdk = 31
        targetSdk = 35
        versionCode = 8
        versionName = "2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = true
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    kotlin {
        jvmToolchain(17)
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
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(libs.firebase.config)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.junit.ktx)
    val osmdroidVersion by extra( "6.1.18")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.compose.material:material:$1.7.0-alpha01")
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.ui)
    implementation(libs.moshi.kotlin)
    implementation(libs.androidx.multidex)
    implementation(libs.lifecycle.runtime.compose)

    //Navigation
    implementation(libs.androidx.navigation.compose)
    
    //Permissions
    implementation(libs.accompanist.permissions)

    //Preview
    debugImplementation (libs.ui.tooling)
    implementation (libs.ui.tooling.preview)
    implementation (libs.androidx.appcompat)

    //Serializable
    implementation(libs.kotlinx.serialization.json.v160)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    //Map
    implementation(libs.osmdroid.android)
    implementation(libs.androidx.activity)
    implementation(libs.osmdroid.mapsforge)
    implementation(libs.osmdroid.wms)

    //Google Service
    implementation (libs.play.services.location)

    //Local unit tests dependencies
    testImplementation(libs.junit)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockk)
    testImplementation (libs.mockwebserver)
    testImplementation (libs.androidx.core.testing)


    //Androidx test dependencies - Instrumented testing dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}