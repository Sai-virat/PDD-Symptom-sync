# Implementation Plan - Fix WasmJs Build Error "Symbol for Any not found"

The build error `org.jetbrains.kotlin.util.FileAnalysisException: Symbol for Any not found` during the analysis of the generated `Res.kt` file for the `wasmJs` target is a known issue in Kotlin 2.1.0. This error typically occurs when the K2 (FIR) compiler cannot correctly resolve the standard library for the Wasm target in generated code or when there are duplicated stdlib KLIBs in the classpath.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/gradle/libs.versions.toml)
- Update `kotlin` version from `2.1.0` to `2.1.10` to benefit from stability fixes in the 2.1.x branch.

#### [MODIFY] [build.gradle.kts (composeApp)](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/build.gradle.kts)
- Uncomment `compose.components.resources` and `compose.components.uiToolingPreview` in `commonMain` dependencies. These are required for Compose Resources to work correctly and their absence while the plugin is applied might be contributing to the issue.
- Add the compiler flag `-Xklib-duplicated-unique-name-strategy=allow-all-with-warning` to the `wasmJs` target's compiler options. This helps the compiler handle potential stdlib duplication issues which often trigger the "Symbol for Any not found" error in Wasm.

## Verification Plan

### Automated Tests
- Run `./gradlew :composeApp:compileKotlinWasmJs` to verify that the Kotlin/Wasm compilation succeeds without the FIR crash.
- Run `./gradlew :composeApp:assembleDebug` to ensure Android build is still working.

### Manual Verification
- Check that the generated `Res.kt` file is now correctly analyzed by the compiler.
