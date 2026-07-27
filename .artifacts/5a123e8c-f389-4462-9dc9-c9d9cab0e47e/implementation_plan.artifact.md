# Implementation Plan - Firebase Firestore Integration

After creating a project in the Firebase Console, we need to connect your Android app to it and migrate our local symptom data to the cloud.

## User Review Required

> [!IMPORTANT]
> **Manual Step Required**: Before I can proceed with any code changes, you MUST:
> 1. In [Firebase Console](https://console.firebase.google.com/), click "Add App" -> "Android".
> 2. Use Package Name: `com.example.symptomsync`.
> 3. Download the `google-services.json` file.
> 4. **Upload or move** `google-services.json` to the `C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/` directory.

## Proposed Changes

### Configuration & Dependencies

#### [MODIFY] [libs.versions.toml](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/gradle/libs.versions.toml)
- Add Firebase BOM version.
- Add `google-services` plugin version.
- Add libraries: `firebase-bom`, `firebase-firestore`.

#### [MODIFY] [build.gradle.kts (Root)](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/build.gradle.kts)
- Add `alias(libs.plugins.google.services) apply false` to the plugins block.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/build.gradle.kts)
- Apply the `google-services` plugin.
- Add Firebase Firestore dependencies using the BOM.

---

### Data Layer Refactoring

#### [MODIFY] [SymptomRepository.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/composeApp/src/commonMain/kotlin/com/example/symptomsync/data/SymptomRepository.kt)
- Create a `FirestoreSymptomRepository` implementation.
- This will switch the app from using the hardcoded `listOf()` to fetching documents from a "symptoms" collection in Firestore.

#### [NEW] [FirestoreSeeder.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/data/FirestoreSeeder.kt)
- A utility to upload our existing 16 symptoms (Fever, Migraine, etc.) to your Firestore database one time, so you don't have to type them manually in the console.

## Verification Plan

### Automated Tests
- Run Gradle build to ensure the Google Services plugin is correctly applied.

### Manual Verification
- Launch the app and check the console logs to see if "Seeding successful" appears.
- Refresh the Firestore Console to see the 16 symptoms populated in the "symptoms" collection.
- Verify that selecting a symptom in the app now fetches its data from the cloud instead of memory.
