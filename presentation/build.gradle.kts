plugins {
    alias(libs.plugins.android.library)
    // alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.todoappnew.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugarJdkLibs)
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.core.ktx)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
    // Media
    implementation(libs.coil.compose)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.compose.calendar)
    api(libs.kotlinx.datetime)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}
