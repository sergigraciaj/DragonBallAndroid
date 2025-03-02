plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.keepcoding.dragonball"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.keepcoding.dragonball"
        minSdk = 24
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(libs.junit)
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}