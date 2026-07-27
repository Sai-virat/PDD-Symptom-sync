import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("stdlib"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(compose.materialIconsExtended)
            implementation("dev.shreyaspatil.generativeai:generativeai-google:0.9.0-1.1.0")
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-wasm-js"))
            }
        }
    }
}

android {
    namespace = "com.example.symptomsync"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.symptomsync"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val buildDirectory = project.layout.buildDirectory
tasks.named("wasmJsBrowserDistribution").configure {
    doLast {
        val buildDir = buildDirectory.asFile.get()
        val destDir = File(buildDir, "dist/wasmJs/productionExecutable")
        // CMP 1.7.0 usually puts skiko files here
        val skikoDir = File(buildDir, "compose/skiko-for-web-runtime")
        if (skikoDir.exists()) {
            skikoDir.listFiles()?.forEach { file ->
                if (file.name == "skiko.js" || file.name == "skiko.wasm") {
                    file.copyTo(File(destDir, file.name), overwrite = true)
                }
            }
        }
    }
}
