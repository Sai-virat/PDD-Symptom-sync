# Task: Distinct AI Diets & Original Reasons

- [x] Refactor `SymptomRepository` with unique data
    - [x] [MODIFY] `composeApp/src/commonMain/kotlin/com/example/symptomsync/data/SymptomRepository.kt`
    - [x] [MODIFY] `app/src/main/java/com/example/symptomsync/data/SymptomRepository.kt`
- [x] Add "Fever" to selection list
    - [x] [MODIFY] `composeApp/src/commonMain/kotlin/com/example/symptomsync/ui/screens/profile/ProfileScreens.kt`
    - [x] [MODIFY] `app/src/main/java/com/example/symptomsync/ui/screens/profile/ProfileScreens.kt`
- [x] Implement Severity-based Diet Selection logic
    - [x] [MODIFY] `composeApp/src/commonMain/kotlin/com/example/symptomsync/ui/viewmodels/CoreFlowViewModel.kt`
    - [x] [MODIFY] `app/src/main/java/com/example/symptomsync/ui/viewmodels/CoreFlowViewModel.kt`
- [x] Verification
    - [x] Select multiple symptoms and verify the diet reflects the most severe one.
    - [x] Verify causes and foods to avoid are specific to selected symptoms.
