import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.ub.utils"
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

val verMoxy = "1.0.13"
val verDagger = "2.24"

dependencies {
    implementation(project(":android"))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    // testing
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    // kapts
    kapt("com.github.moxy-community:moxy-compiler:$verMoxy")
    kapt("com.google.dagger:dagger-compiler:$verDagger")
}
