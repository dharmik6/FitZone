plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.fitzone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fitzone"
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

    //shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    //image circle
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //number picker
    implementation ("io.github.ShawnLin013:number-picker:2.4.13")

    //graph chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //BMI
    implementation ("com.github.Gruzer:simple-gauge-android:0.3.1")




    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}