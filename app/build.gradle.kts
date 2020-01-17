import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.ub.utils"
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val verMoxy = "2.0.2"
val verDagger = "2.25.4"
val verRetrofit = "2.6.4"
val verCoroutines = "1.3.3"

dependencies {
    implementation(project(":android"))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    // testing
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    // kapts
    kapt("com.github.moxy-community:moxy-compiler:$verMoxy")
    kapt("com.google.dagger:dagger-compiler:$verDagger")

    // android x
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    // moxy
    implementation("com.github.moxy-community:moxy:$verMoxy")
    implementation("com.github.moxy-community:moxy-androidx:$verMoxy")
    implementation("com.github.moxy-community:moxy-material:$verMoxy")

    // retrofit 2
    implementation("com.squareup.retrofit2:retrofit:$verRetrofit")
    implementation("com.squareup.retrofit2:converter-gson:$verRetrofit")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$verRetrofit")

    // logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // rx android
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.17")

    // dagger 2
    implementation("com.google.dagger:dagger:$verDagger")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$verCoroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$verCoroutines")
}
