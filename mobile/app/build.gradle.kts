import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val localProps = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localPropsFile.inputStream().use { localProps.load(it) }
}

val apiScheme = localProps.getProperty("API_SCHEME") ?: "http"
val apiHost   = localProps.getProperty("API_HOST") ?: "10.0.2.2"
val apiPort   = localProps.getProperty("API_PORT") ?: "8080"

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

        buildConfigField("String", "API_SCHEME", "\"$apiScheme\"")
        buildConfigField("String", "API_HOST", "\"$apiHost\"")
        buildConfigField("String", "API_PORT", "\"$apiPort\"")
        buildConfigField("String", "BASE_URL", "\"$apiScheme://$apiHost:$apiPort/\"")
        manifestPlaceholders["DL_HOST"] = apiHost
        manifestPlaceholders["DL_PORT"] = apiPort
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
        buildConfig = true
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

    //WEBSOCKET DEP
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.preference:preference:1.2.1")
}