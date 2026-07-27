# Implementation Plan - Migration to Compose Multiplatform (Web & Android)

Restructure the project into a Kotlin Multiplatform (KMP) project to support both Android and Web (Wasm) targets with a shared codebase.

## User Review Required

> [!IMPORTANT]
> **This is a major architectural change.**
> - Your code will move from the `:app` module to a new `:composeApp` module.
> - We will replace Android-specific libraries (like `androidx.navigation`) with Multiplatform ones (`org.jetbrains.compose.navigation`).
> - This ensures that **"One fix = Both fixed"**. A change in your logic will update both your phone and the web version instantly.

## Proposed Changes

### 1. Dependency Management
#### [MODIFY] [libs.versions.toml](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/gradle/libs.versions.toml)
- Add Compose Multiplatform plugin version.
- Add multiplatform dependencies for Compose UI, Material3, Navigation, and Lifecycle.

### 2. Project Structure
#### [MODIFY] [settings.gradle.kts](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/settings.gradle.kts)
- Include the new `:composeApp` module.

#### [NEW] [composeApp/build.gradle.kts](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/build.gradle.kts)
- Configure `kotlin("multiplatform")` with `androidTarget()` and `wasmJs()`.
- Define shared dependencies in `commonMain`.

### 3. Code Migration
#### [MOVE] Core Logic & UI
- Move all ViewModels, Screens, and Themes to `composeApp/src/commonMain/kotlin/com/example/symptomsync/`.
- **Fix**: Replace `android.util.Patterns` in `AuthViewModel` with a standard Regex for email validation.

#### [NEW] [App.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/commonMain/kotlin/com/example/symptomsync/App.kt)
- Create a shared `App()` composable that hosts the `SymptomSyncNavHost`. This will be used by both Android and Web.

### 4. Target Setup
#### [ANDROID] [MainActivity.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/androidMain/kotlin/com/example/symptomsync/MainActivity.kt)
- Update the Android entry point to call the shared `App()` function.

#### [WEB] [main.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/wasmJsMain/kotlin/main.kt) & [index.html](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/wasmJsMain/resources/index.html)
- Set up the entry point for the browser to render the Compose UI.

## Verification Plan

### Automated Tests
- Build the project: `./gradlew assemble`.
- Run the web version: `./gradlew wasmJsBrowserRun`.

### Manual Verification
- Open the web app on your laptop.
- Open the android app on your phone.
- Confirm both show the exact same Login/Register screens and validation logic.
