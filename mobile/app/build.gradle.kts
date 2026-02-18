plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ftn.mrs_team11_gorki"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "ftn.mrs_team11_gorki"
        minSdk = 30
        targetSdk = 36
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

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.navigation:navigation-ui:2.7.7")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //noinspection UseTomlInstead,NewerVersionAvailable
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //noinspection UseTomlInstead,NewerVersionAvailable
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //noinspection UseTomlInstead,NewerVersionAvailable
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //noinspection UseTomlInstead,GradleDependency
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.2")
    //noinspection UseTomlInstead,NewerVersionAvailable
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.preference:preference:1.2.1")
}