plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.cs4730.broadcastnoti"
    compileSdk = 37

    defaultConfig {
        applicationId = "edu.cs4730.broadcastnoti"
        minSdk = 32
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.activity)
}
