plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // KSP permite que Room genere código automáticamente
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tecsup.devtrack"

    // Android API requerida por las librerías actuales
    compileSdk = 37

    defaultConfig {
        applicationId = "com.tecsup.devtrack"

        // Android 8.0 (Oreo)
        minSdk = 26

        // API objetivo
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Permite usar viewModel() directamente en Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.compose.material3)

    // ROOM
    // Base de datos local para guardar proyectos en el dispositivo
    implementation(libs.androidx.room.runtime)

    // Extensiones Kotlin para usar Room con corrutinas y Flow
    implementation(libs.androidx.room.ktx)

    // Compilador de Room usando KSP
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
}