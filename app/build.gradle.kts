import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val secretProperties = Properties()
val secretPropertiesFile = rootProject.file("secrets.properties")
if (secretPropertiesFile.exists()) secretProperties.load(secretPropertiesFile.inputStream())

android {
    namespace = "com.voltfitness.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.voltfitness.app"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "EXERCISE_DB_API_KEY",
            "\"${secretProperties.getProperty("EXERCISE_DB_API_KEY", "")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
            freeCompilerArgs.addAll(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Compose Compiler Config
    composeCompiler {
        featureFlags.set(setOf(org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.StrongSkipping))
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/MANIFEST.MF"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    // ========== COMPOSE & UI ==========
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ========== COIL - IMAGE LOADING ==========
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-gif:2.7.0")


    // ========== CORE ANDROID ==========
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // ========== RETROFIT ==========
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx)
    implementation(libs.okhttp.logging)

    // ========== DATABASE (Room) ==========
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // ========== DEPENDENCY INJECTION (Hilt) ==========
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // ========== COROUTINES ==========
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)

    // ========== DATA PERSISTENCE ==========
    implementation(libs.androidx.datastore.preferences)

    // ========== LOGGING ==========
    implementation(libs.timber)

    // ========== SERIALIZATION ==========
    implementation(libs.kotlinx.serialization.json)

    // ========== TESTING ==========
    testImplementation(libs.junit)
    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.engine)
    testImplementation(libs.junit5.params)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// KSP Configuration (Kotlin Symbol Processing)
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}

// Testing with JUnit 5
tasks.withType<Test> {
    useJUnitPlatform()
}
