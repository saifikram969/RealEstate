plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}


android {
    namespace = "com.btjnonbrokerage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.btjnonbrokerage"
        minSdk = 24
        targetSdk = 35
        versionCode = 7
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
            signingConfig = signingConfigs.getByName("debug")
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


}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.databinding.runtime)
//    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

     //County code Piker
    implementation("com.hbb20:ccp:2.5.1")
    //sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    // Navigation components
    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // glide dependency
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //circular Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // image slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    implementation ("com.google.android.libraries.places:places:3.5.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    //flextlayout
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    //shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    //pinview
    implementation ("io.github.chaosleung:pinview:1.4.4")
    //pay U
    implementation ("in.payu:payu-checkout-pro:2.5.0")

    implementation("com.google.firebase:firebase-messaging:24.0.1")

    //    CashFree Payment Gateway
   // val cashFreeSdkVersion = "2.1.9"
   // implementation("com.cashfree.pg:api:$cashFreeSdkVersion")
    implementation ("com.cashfree.pg:api:2.1.25")

    //Razorpay Payment Gateway
    implementation (libs.checkout)
    //OSM map
    implementation ("org.osmdroid:osmdroid-android:6.1.13")


}





