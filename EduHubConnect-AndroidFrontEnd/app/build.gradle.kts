plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.connect.eduhubconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.connect.eduhubconnect"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // ✅ Navigation (Updated to compatible versions)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // ✅ Hilt Dependency Injection
    val hilt_version = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // ✅ Dagger dependencies (Remove duplicate versions)
    implementation("com.google.dagger:dagger:$hilt_version")
    kapt("com.google.dagger:dagger-compiler:$hilt_version")

    // ✅ Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // ✅ Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // ✅ Room Database
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // ✅ Kotlin Coroutines
    val coroutines_version = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // ✅ Glide (Image Loading)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    //✅ Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging:24.1.0")

    //for circular image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //glide
    implementation("com.squareup.okhttp3:okhttp:4.10.0")  // Adjust to the latest version
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")

    implementation("org.java-websocket:Java-WebSocket:1.5.3")
//    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
//    implementation("org.springframework:spring-websocket:5.3.7") // For WebSocket with STOMP
//    implementation("org.springframework:spring-messaging:5.3.7") // For STOMP protocol support
//    implementation("com.github.mozzarella:spring-stomp-android:1.0.1") // For Android STOMP support
    //

    // https://mvnrepository.com/artifact/com.github.NaikSoftware/StompProtocolAndroid
//    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
//    implementation("ua.naiksoftware:stomp-client:1.6.6")

}
