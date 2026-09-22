plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.cs4730.broadcastboot"
    compileSdk = 37

    defaultConfig {
        applicationId = "edu.cs4730.broadcastboot"
        minSdk = 32
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.activity)
}
