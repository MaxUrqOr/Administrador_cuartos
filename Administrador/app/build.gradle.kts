plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.makao.administrador"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.makao.administrador"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.firebase:firebase-messaging:23.0.0")

    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.etebarian:meow-bottom-navigation:1.2.0")

    implementation ("com.airbnb.android:lottie:4.1.0")
    implementation ("com.google.code.gson:gson:2.8.8")

    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
}