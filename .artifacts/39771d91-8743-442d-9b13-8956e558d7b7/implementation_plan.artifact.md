# Implementation Plan: Attribution and Database Expansion

This plan covers two main goals:
1. **Explicit Attribution**: Clearly showing which symptom a cause or meal plan item is addressing.
2. **Database Expansion**: Adding ~25-30 symptoms to Firestore with detailed causes and diet plans.

## Proposed Changes

### [Component] ViewModels & Models

#### [MODIFY] [CoreFlowViewModel.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/ui/viewmodels/CoreFlowViewModel.kt)
- Update `PossibleCause` to include `parentSymptom`.
- Update `MealPlanItem` to include `targetingSymptom`.
- Update `generateSystemPlan()` to group causes by symptom.
- Update `getMealDetail()` to return info about which symptom the meal is for.

### [Component] Database (Python)

#### [MODIFY] [migrate_data.py](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/backend/migrate_data.py)
- Expand `SYMPTOMS_DATA` list with ~20 additional symptoms (Total ~30).
- Include detailed `possibleCauses`, `dietPlan`, and `foodsToAvoid` for each.
- Run the script to update Firestore.

### [Component] UI Screens

#### [MODIFY] [CoreFlowScreens.kt](file:///C:/Users/ASUS/AndroidStudioProjects/Symptomsync3/app/src/main/java/com/example/symptomsync/ui/screens/core/CoreFlowScreens.kt)
- **Analysis Screen**: Show a summary of why the system picked certain insights.
- **Possible Causes Screen**: Add a "Related to: [Symptom]" tag on each card.
- **Diet Plan Screen**: Show which symptom each meal is targeting (e.g., "Breakfast (for Acidity)").

---

## Verification Plan

### Manual Verification
1. Run `migrate_data.py` and check Firebase Console for 25+ entries.
2. Open the app and verify the "Add Symptoms" list is long and searchable.
3. Select "Fever" and "Cough", verify causes are labeled separately.
4. Verify Meal Details show the correct targeting symptom.
