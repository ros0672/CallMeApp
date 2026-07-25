plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.callme.security"
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

}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:di"))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.kotlinx.coroutines.core)

    // EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.core.ktx)
}