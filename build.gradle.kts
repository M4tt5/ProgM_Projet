// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //alias(libs.plugins.kotlin.compose) apply false
    // Ajoutez ce plugin pour le version catalog
    alias(libs.plugins.kotlin.serialization) apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}