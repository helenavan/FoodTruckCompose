plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
