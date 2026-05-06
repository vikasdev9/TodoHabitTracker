plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}