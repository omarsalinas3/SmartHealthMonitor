plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

android {
    namespace   = "mx.utng.smarthealthmonitor.tv"
    compileSdk  = 35

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk        = 21
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
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
        compose = true
    }
    packaging {
        resources {
            excludes += "META-INF/androidx.emoji2_emoji2.version"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Leanback Library — BrowseSupportFragment (S11)
    implementation("androidx.leanback:leanback:1.2.0")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // ViewModel + Fragment KTX + Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // ── Compose for TV — S12 ──────────────────────────────────────────
    // Activity Compose (ComponentActivity + setContent)
    implementation("androidx.activity:activity-compose:1.9.3")
    // Compose BOM — versiones consistentes
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.runtime:runtime")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Compose for TV (Iviews, cards, focused border)
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0-rc01")
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.5")
    // Lifecycle Compose (collectAsStateWithLifecycle)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    // ExoPlayer (S12 — TvPlaybackScreen)
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // Eclipse Paho MQTT para Android
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    // Kotlinx Serialization para JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
