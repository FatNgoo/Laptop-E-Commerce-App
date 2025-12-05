plugins {
    alias(libs.plugins.android.application)
    // Temporarily disabled - no Firebase in mock app
    // id("com.google.gms.google-services")
}

android {
    namespace = "com.example.otech"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.otech"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    
    // OSMDroid for offline maps
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    
    // Gson for Room TypeConverter
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Firebase dependencies - disabled for mock app
    // implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    // implementation("com.google.firebase:firebase-analytics")
}