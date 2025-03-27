plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.weekly_recipe_planner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weekly_recipe_planner"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.firebase:firebase-database:20.2.1")
    implementation ("com.google.firebase:firebase-storage:20.2.0")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.fragment:fragment:1.3.6")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    androidTestImplementation(libs.espresso.core)
}