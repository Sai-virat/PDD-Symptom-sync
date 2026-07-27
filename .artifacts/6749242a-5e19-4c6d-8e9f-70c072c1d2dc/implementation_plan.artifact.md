# Implementation Plan - Secure AI Integration

Securely integrating Gemini AI by moving the API key to `local.properties` and providing clear user guidance.

## Proposed Changes

### [Root Project]
#### [MODIFY] [local.properties](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/local.properties)
- Add a placeholder for `GEMINI_API_KEY`.

### [composeApp]
#### [MODIFY] [build.gradle.kts](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/build.gradle.kts)
- Add logic to read the API key from `local.properties` and inject it as a BuildConfig or BuildConfig-like field (using `compose.compiler.options` or similar, or just a simple generated file).
- Actually, for KMP, a simple way is to use a generated file or `BuildKonfig` library. Given the simplicity, I will use a generated Kotlin file approach.

#### [MODIFY] [GeminiRepository](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/commonMain/kotlin/com/example/symptomsync/data/GeminiRepository.kt)
- Update to use the dynamically injected key.

## User Steps to Get the Key
1. Go to [Google AI Studio](https://aistudio.google.com/app/apikey).
2. Click **Create API key in new project**.
3. Copy the key and paste it here in the chat.

## Verification Plan
- Once the key is provided, I will verify the AI connection by attempting a simple mock prompt call.
