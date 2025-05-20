import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.example.resqr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.resqr"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${properties.getProperty("SUPABASE_URL", "")}\""
        )
        buildConfigField(
            "String",
            "SUPABASE_KEY",
            "\"${properties.getProperty("SUPABASE_KEY", "")}\""
        )

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // or latest

    // ViewModel support for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Kotlin coroutines (if you're using them in the ViewModel)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")


    //ktor
    implementation("io.ktor:ktor-client-android:3.1.1")

    //supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    // Navigation for Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.8.9")


    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    //map
    implementation("org.osmdroid:osmdroid-android:6.1.20")

    //coil
    implementation ("io.coil-kt:coil-compose:2.6.0")

    //or code
    implementation ("com.google.zxing:core:3.5.2")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
}