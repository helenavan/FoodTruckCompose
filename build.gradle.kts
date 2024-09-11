buildscript {
    repositories {
        google()
        mavenCentral()
    }
    val kotlinVersion by extra("1.9.0")
    val gradleVersion by extra("8.5.2")
    val googleServicesVersion by extra("4.3.14")

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:$googleServicesVersion")
    }
}
