# Implementation Plan: Project Cleanup and Bug Fixes

Clean up redundant code, fix memory leaks, and standardize the project structure to ensure better performance and maintainability.

## User Review Required

> [!IMPORTANT]
> **Redundant Modules**: I noticed both `:app` (Standard Android) and `:composeApp` (KMP/Compose Multiplatform) exist. Most recent work has been in `:app`. Should I delete `:composeApp` and the `web` folder if they are not being used?

> [!WARNING]
> **Secrets in Git**: I will completely scrub the git history to ensure no API keys or service account JSONs are being tracked, which will prevent GitHub's "Push Protection" blocks.

## Proposed Changes

### [Component] Android App (:app)

#### [MODIFY] [SymptomRepository.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/data/SymptomRepository.kt)
- Fix memory leak: Move `FirebaseFirestore` initialization out of the static `object` or access it via a provider to avoid holding a reference to a potentially stale context.

#### [MODIFY] [GeminiRepository.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/data/GeminiRepository.kt)
- Deduplicate data classes: Combine `AIMeal` and `AIDietMeal` into a single `MealSpec`.
- Refactor to use modern Gemini model naming if necessary.

#### [MODIFY] [CoreFlowViewModel.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/ui/viewmodels/CoreFlowViewModel.kt)
- Clean up unused variables and properties (e.g., `primary` in `generateSystemPlan`).
- Standardize error handling for database fetches.

#### [MODIFY] [CoreFlowScreens.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/ui/screens/core/CoreFlowScreens.kt)
- Ensure consistent naming: Use "Health Insights" everywhere since AI was disabled as per previous request.

### [Component] Git & Environment

#### [MODIFY] [.gitignore](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/.gitignore)
- Add more robust rules to exclude all `build/`, `node_modules/`, and `.next/` directories.

#### [EXECUTE] Git Scrub
- Run `git filter-repo` or similar if available, or simply re-initialize the repo without large binary blobs.

---

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to ensure no compilation errors.
- Run `migrate_data.py` to ensure the database link remains functional.

### Manual Verification
- Verify in Android Studio "Inspections" that the memory leak warning in `SymptomRepository` is gone.
- Check that the "Diet Plan" correctly shows multiple entries when multiple symptoms are selected.
