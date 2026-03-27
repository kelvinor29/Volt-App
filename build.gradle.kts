plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}

tasks.register<Delete>("clean") {
    group = "build"
    description = "Delete the build directory"
    delete(rootProject.layout.buildDirectory)
}
