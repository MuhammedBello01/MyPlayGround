plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.emperormoh.myplayground"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.emperormoh.myplayground"
        minSdk = 26
        targetSdk = 35
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.media3.common.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil.compose)
    implementation(libs.android.material)

    implementation(libs.androidx.foundation)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.gson)

    implementation(libs.androidx.material.icons.extended)

    implementation (libs.onnxruntime.android)
    implementation(libs.tensorflow.lite)
    implementation(files("libs/mytflitesdk-release.aar"))


    implementation (libs.androidx.runtime.livedata)
    implementation (libs.androidx.runtime)
    implementation (libs.lottie.compose)

    implementation(libs.androidx.material)

    implementation (libs.androidx.biometric)
    implementation (libs.ui)

    implementation (libs.androidx.media)

    implementation(libs.porcupine.android) // Check for latest version
    implementation (libs.kotlinx.coroutines.android)

    implementation (libs.vosk.android)



    // CameraX (for camera)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

// ML Kit for OCR (Google's text recognition)
    implementation(libs.play.services.mlkit.text.recognition)


    //implementation(libs.androidx.scenecore)



    //implementation (libs.vosk.vosk.android)
//    implementation(libs.accompanist.pager)
//    implementation(libs.accompanist.pager.indicators)
}