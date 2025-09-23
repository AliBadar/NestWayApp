import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hiltAndroid)
    kotlin("kapt")
    alias(libs.plugins.google.gms.google.services)
}

val sdkVersionFile = file("../gradle.properties")
val properties = Properties()
properties.load(sdkVersionFile.inputStream())
val agoraSdkVersion = properties.getProperty("rtc_sdk_version")

android {
    namespace = "com.example.hackatonprjoect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hackatonprjoect"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        val AGORA_APP_ID = properties.getProperty("AGORA_APP_ID", "")
        if (AGORA_APP_ID == "") {
            throw GradleException("Please configure correctly in the local.properties file in the project root directory: AGORA_APP_ID=<Your Agora AppId>")
        }
        val AGORA_APP_CERT = properties.getProperty("AGORA_APP_CERT", "")
        buildConfigField("String", "AGORA_APP_ID", "\"$AGORA_APP_ID\"")
        buildConfigField("String", "AGORA_APP_CERT", "\"$AGORA_APP_CERT\"")
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.browser)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.androidx.material3.v130)
    implementation(libs.material.icons.extended)

    implementation(libs.sdp.compose)
    implementation(libs.coil.compose) // For loading images
    implementation(libs.coil.gif) // For loading images
    implementation(libs.coil.svg) // Add this for SVG support


    // VisioMoveEssential
    implementation(files("../VisioMoveEssential.aar"))

    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)
    implementation(libs.hiltWork)
    kapt(libs.hiltCompiler)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.barcode.scanning)
    implementation("io.agora.rtc:full-sdk:${agoraSdkVersion}")
    implementation("io.agora.rtc:full-screen-sharing:${agoraSdkVersion}")

    implementation("com.google.zxing:core:3.4.1")  // ZXing core for QR scanning
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")  // For ZXing integration
    implementation(libs.workRuntimeKtx)
    implementation(libs.androidx.room.runtime.v261)
    implementation(libs.androidx.room.ktx.v261)
    kapt(libs.androidx.room.compiler.v261)

    implementation("androidx.datastore:datastore-preferences-core:1.1.2")
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    implementation(libs.firebase.messaging)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("org.altbeacon:android-beacon-library:2.21.1")
    implementation("com.davidgyoungtech:beacon-parsers:1.0")


}