plugins {
    id("com.android.application") // Apply Android Application plugin
    id("org.jetbrains.kotlin.android") // Apply Kotlin plugin
}

configurations.all {
    resolutionStrategy {
        force ("androidx.core:core:1.15.0")
    }
}


repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io") // For some third-party libraries
    maven("https://maven.google.com")
}

android {
    namespace = "com.example.fyp22"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fyp22"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Correct reference to core-ktx library
    implementation(libs.androidx.lifecycle.runtime.ktx) // Correct reference to lifecycle-runtime-ktx
    implementation(libs.androidx.appcompat) // Correct reference to appcompat
    implementation(libs.androidx.activity.compose) // Correct reference to activity-compose
    implementation(platform(libs.androidx.compose.bom)) // Correct BOM for compose libraries
    implementation(libs.androidx.ui) // Correct reference to ui library
    implementation(libs.androidx.ui.graphics) // Correct reference to ui-graphics
    implementation(libs.androidx.ui.tooling.preview) // Correct reference to ui-tooling-preview
    implementation(libs.androidx.material3) // Correct reference to material3
    implementation(libs.opencsv)
    implementation(libs.gson)
    implementation(libs.graphview)



    // Correct reference for ConstraintLayout
    implementation(libs.androidx.constraintlayout) // Correct reference to constraintlayout

    // MPAndroidChart should be added only once
    implementation(libs.mpandroidchart){
     exclude(group = "com.android.support", module = "support-compat")}
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
