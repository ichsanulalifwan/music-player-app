import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.app.distribution)
}

val localProperties = Properties().apply {
    val file = File(rootProject.rootDir, "local.properties")
    if (file.exists()){
        load(FileInputStream(file))
    } else {
        error("please add local.properties and key-value on root project")
    }
}

android {
    namespace = "com.example.musicplayerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.musicplayerapp"
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
    firebaseAppDistribution {
        appId = localProperties.getProperty("app.id")
        serviceCredentialsFile = firebaseCredentialsFile()
        releaseNotes = firebaseDistAppReleaseNote("[App Distribution] ")
        groups = "tester"
    }
}

dependencies {
//    implementation(project.core.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(platform(libs.firebase.bom))

    // DI
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
}

fun firebaseCredentialsFile(): String{
    return "app/serviceAccountKey.json"
}

fun firebaseDistAppReleaseNote(flavor: String): String{
    return localProperties.getProperty("release.note") ?: (flavor + "") // custom release note
}