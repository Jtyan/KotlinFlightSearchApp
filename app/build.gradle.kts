plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    /*
    Why do you need this plugin?
    Because room-compiler is not a normal library — it’s an annotation processor.
    So it needs ksp plugin to set up the KSP processor
    */
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobufDataStore)
}

android {
    namespace = "learningprogramming.academy.kotlinflightsearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "learningprogramming.academy.kotlinflightsearchapp"
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

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    //KSP is a powerful and yet simple API for parsing Kotlin annotations
    ksp(libs.androidx.room.compiler)
    //DataStore-Preferences
    implementation(libs.androidx.datastore.preferences)
    //Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Navigation
    implementation(libs.androidx.navigation.compose)
    //DataStore-Protobuf
    implementation (libs.androidx.datastore)
    implementation (libs.androidx.datastore.core)
    implementation (libs.protobuf.javalite)
//    implementation(libs.androidx.datastore.protobuf)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite") // important for Android - use "lite" not "full" protobuf
                }
            }
        }
    }
}