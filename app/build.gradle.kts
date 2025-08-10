// Archivo build.gradle.kts actualizado para una aplicación de Android con Compose.
// Se han eliminado las dependencias de escritorio que causaban conflictos de clases duplicadas.

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Plugins aplicados desde el archivo de la raíz
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.todolist"
    compileSdk = 35
    // Usamos la versión 34 que es compatible con el plugin de Gradle
    defaultConfig {
        applicationId = "com.example.todolist"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    // Este bloque ya no es necesario, el plugin de Kotlin Compose maneja la versión automáticamente.
    // composeOptions {
    //     kotlinCompilerExtensionVersion = "1.6.0"
    // }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "35.0.1"
}

dependencies {
    // Se ha eliminado la dependencia de escritorio 'ui-desktop' que causaba el conflicto.

    // Dependencias principales de Compose para Android.
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // Retrofit y OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Jetpack Security y Biometrics
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.biometric:biometric:1.1.0")

    // Dependencias para Markdown Texto enriquecido
    implementation("com.halilibo.compose-richtext:richtext-ui-material3:0.16.0")
    implementation("com.halilibo.compose-richtext:richtext-commonmark:0.16.0")

    // Dependencia para la serialización JSON de Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Dependencias del catálogo de versiones (libs.versions.toml)
    // Se ha eliminado una referencia duplicada a `foundation.layout.android`.
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.espresso.core)

    // Dependencias de prueba
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
