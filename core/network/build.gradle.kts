plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.callme.network"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":core:security"))

    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Retrofit
    api(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    // OkHttp
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
    // Moshi
    api(libs.moshi)
    implementation(libs.moshi.kotlin)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
}