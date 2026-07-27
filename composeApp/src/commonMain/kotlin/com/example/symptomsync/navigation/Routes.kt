package com.example.symptomsync.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    // Phase 1: Auth
    @Serializable data object Splash : Screen
    @Serializable data object Login : Screen
    @Serializable data object Register : Screen

    // Phase 2: Health Profile Setup
    @Serializable data object BasicInfo : Screen
    @Serializable data object DietaryPreferences : Screen
    @Serializable data object FitnessGoals : Screen
    @Serializable data object SymptomSelection : Screen
    @Serializable data object SeverityScale : Screen
    @Serializable data object ActivityLevel : Screen
    @Serializable data object AIProcessing : Screen
    @Serializable data object ProfileSuccess : Screen

    // Phase 3: Dashboard & Planning
    @Serializable data object Home : Screen
    @Serializable data object DailyDietPlan : Screen
    @Serializable data class MealDetail(val mealId: String) : Screen
    @Serializable data object MealSwap : Screen
    @Serializable data object SymptomLogger : Screen
    @Serializable data object WaterTracker : Screen
    @Serializable data object GroceryList : Screen
    @Serializable data object RecipeCookingMode : Screen

    // Phase 4: Analytics
    @Serializable data object AnalyticsHub : Screen
    @Serializable data object SymptomCorrelation : Screen
    @Serializable data object NutritionalBreakdown : Screen
    @Serializable data object WeightProgress : Screen
    @Serializable data object AICoachChat : Screen
    @Serializable data object HistoryLog : Screen
    @Serializable data object FavoritesLibrary : Screen
    @Serializable data object CustomRecipeCreator : Screen

    // Phase 5: Settings
    @Serializable data object UserProfile : Screen
    @Serializable data object EditHealthProfile : Screen
    @Serializable data object DietaryGoalAdjuster : Screen
    @Serializable data object AppPreferences : Screen
    @Serializable data object NotificationsCenter : Screen
    @Serializable data object FAQCenter : Screen
    @Serializable data object PrivacyAbout : Screen

    // Core Flow (Requested Design)
    @Serializable data object CoreHome : Screen
    @Serializable data object CoreSymptomSelect : Screen
    @Serializable data object CoreAnalysis : Screen
    @Serializable data object CoreCauses : Screen
    @Serializable data object CoreDietPlan : Screen
    @Serializable data object CoreWaterTracker : Screen
    @Serializable data object CoreProgress : Screen
    @Serializable data object CoreHistory : Screen
    @Serializable data class CoreMealDetail(val mealType: String) : Screen
}
