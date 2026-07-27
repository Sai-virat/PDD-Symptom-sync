package com.example.symptomsync.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.symptomsync.ui.screens.analytics.*
import com.example.symptomsync.ui.screens.auth.*
import com.example.symptomsync.ui.screens.core.*
import com.example.symptomsync.ui.screens.dashboard.*
import com.example.symptomsync.ui.screens.profile.*
import com.example.symptomsync.ui.screens.settings.*
import com.example.symptomsync.ui.viewmodels.*

@Composable
fun SymptomSyncNavHost(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: HealthProfileViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()
    val analyticsViewModel: AnalyticsViewModel = viewModel()
    val coreViewModel: CoreFlowViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Splash) {
        // Phase 1
        composable<Screen.Splash> { SplashScreen(onTimeout = { navController.navigate(Screen.Login) }) }
        composable<Screen.Login> { 
            LoginScreen(
                authViewModel, 
                onLoginSuccess = { 
                    dashboardViewModel.mockFetchUserData()
                    navController.navigate(Screen.CoreHome) 
                }, 
                onSignUpClick = { navController.navigate(Screen.Register) }
            ) 
        }
        composable<Screen.Register> { 
            RegistrationScreen(
                authViewModel, 
                onRegisterSuccess = { 
                    dashboardViewModel.mockFetchUserData()
                    navController.navigate(Screen.BasicInfo) 
                }, 
                onLoginClick = { navController.navigate(Screen.Login) }
            ) 
        }

        // Core Flow
        composable<Screen.CoreHome> { 
            CoreHomeScreen(
                userName = dashboardViewModel.userName,
                onAddSymptom = { navController.navigate(Screen.CoreSymptomSelect) },
                onWaterClick = { navController.navigate(Screen.CoreWaterTracker) },
                onDietPlanClick = { navController.navigate(Screen.CoreDietPlan) },
                onProgressClick = { navController.navigate(Screen.CoreProgress) },
                onHistoryClick = { navController.navigate(Screen.CoreHistory) }
            ) 
        }
        composable<Screen.CoreSymptomSelect> { 
            CoreSymptomSelectScreen(coreViewModel, onContinue = { navController.navigate(Screen.CoreAnalysis) }) 
        }
        composable<Screen.CoreAnalysis> { 
            CoreAnalysisScreen(coreViewModel, onViewCauses = { navController.navigate(Screen.CoreCauses) }, onGetDietPlan = { navController.navigate(Screen.CoreDietPlan) }) 
        }
        composable<Screen.CoreCauses> { 
            CoreCausesScreen(coreViewModel, onGetDietPlan = { navController.navigate(Screen.CoreDietPlan) }) 
        }
        composable<Screen.CoreDietPlan> { 
            CoreDietPlanScreen(coreViewModel, onMealClick = { type -> navController.navigate(Screen.CoreMealDetail(type)) }) 
        }
        composable<Screen.CoreMealDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.CoreMealDetail>()
            CoreMealDetailScreen(route.mealType, coreViewModel, onBack = { navController.popBackStack() })
        }
        composable<Screen.CoreWaterTracker> { 
            CoreWaterTrackerScreen(coreViewModel, onDone = { navController.popBackStack() }) 
        }
        composable<Screen.CoreProgress> { 
            CoreProgressScreen(coreViewModel, onBack = { navController.popBackStack() }) 
        }
        composable<Screen.CoreHistory> { 
            CoreHistoryScreen(onBack = { navController.popBackStack() }) 
        }

        // Phase 2
        composable<Screen.BasicInfo> { BasicInfoScreen(profileViewModel, onNext = { navController.navigate(Screen.DietaryPreferences) }) }
        composable<Screen.DietaryPreferences> { DietaryPreferencesScreen(profileViewModel, onNext = { navController.navigate(Screen.FitnessGoals) }) }
        composable<Screen.FitnessGoals> { FitnessGoalsScreen(profileViewModel, onNext = { navController.navigate(Screen.SymptomSelection) }) }
        composable<Screen.SymptomSelection> { SymptomSelectionScreen(profileViewModel, onNext = { navController.navigate(Screen.SeverityScale) }) }
        composable<Screen.SeverityScale> { SeverityScaleScreen(profileViewModel, onNext = { navController.navigate(Screen.ActivityLevel) }) }
        composable<Screen.ActivityLevel> { ActivityLevelScreen(profileViewModel, onNext = { navController.navigate(Screen.AIProcessing) }) }
        composable<Screen.AIProcessing> { AIProcessingScreen(profileViewModel, onComplete = { navController.navigate(Screen.ProfileSuccess) }) }
        composable<Screen.ProfileSuccess> { 
            ProfileSuccessScreen(onGoToDashboard = { 
                dashboardViewModel.mockFetchUserData()
                navController.navigate(Screen.Home) 
            }) 
        }

        // Phase 3
        composable<Screen.Home> { MainDashboard(dashboardViewModel, onMealClick = { meal -> navController.navigate(Screen.MealDetail(meal.id)) }, onLogSymptom = { navController.navigate(Screen.SymptomLogger) }) }
        composable<Screen.DailyDietPlan> { DailyDietPlanScreen(dashboardViewModel, onMealClick = { meal -> navController.navigate(Screen.MealDetail(meal.id)) }) }
        composable<Screen.MealDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.MealDetail>()
            val meal = dashboardViewModel.dailyMeals.find { it.id == route.mealId } ?: dashboardViewModel.dailyMeals.first()
            MealDetailScreen(meal, onSwap = { navController.navigate(Screen.MealSwap) }, onBack = { navController.popBackStack() })
        }
        composable<Screen.MealSwap> { Text("Meal Swap Screen Placeholder") }
        composable<Screen.SymptomLogger> { SymptomLoggerScreen(dashboardViewModel, onSubmit = { navController.popBackStack() }) }
        composable<Screen.WaterTracker> { WaterTrackerScreen(dashboardViewModel) }
        composable<Screen.GroceryList> { GroceryListScreen(dashboardViewModel) }
        composable<Screen.RecipeCookingMode> { RecipeCookingScreen(dashboardViewModel, onFinish = { navController.popBackStack() }) }

        // Phase 4
        composable<Screen.AnalyticsHub> { AnalyticsHubScreen(analyticsViewModel) }
        composable<Screen.SymptomCorrelation> { SymptomCorrelationScreen() }
        composable<Screen.AICoachChat> { AICoachChatScreen(analyticsViewModel) }
        composable<Screen.CustomRecipeCreator> { CustomRecipeCreatorScreen(analyticsViewModel, onSave = { navController.popBackStack() }) }

        // Phase 5
        composable<Screen.UserProfile> { 
            UserProfileScreen(
                viewModel = profileViewModel,
                onNavigate = { routeName ->
                    when (routeName) {
                        "EditHealthProfile" -> navController.navigate(Screen.EditHealthProfile)
                        "DietaryGoalAdjuster" -> navController.navigate(Screen.DietaryGoalAdjuster)
                        "AppPreferences" -> navController.navigate(Screen.AppPreferences)
                        "NotificationsCenter" -> navController.navigate(Screen.NotificationsCenter)
                        "FAQCenter" -> navController.navigate(Screen.FAQCenter)
                        "PrivacyAbout" -> navController.navigate(Screen.PrivacyAbout)
                    }
                },
                onLogout = { 
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) 
        }
        composable<Screen.EditHealthProfile> { EditHealthProfileScreen(profileViewModel, onBack = { navController.popBackStack() }) }
        composable<Screen.DietaryGoalAdjuster> { DietaryGoalAdjusterScreen(profileViewModel, onBack = { navController.popBackStack() }) }
        composable<Screen.AppPreferences> { AppPreferencesScreen(profileViewModel, onBack = { navController.popBackStack() }) }
        composable<Screen.NotificationsCenter> { NotificationsCenterScreen(profileViewModel, onBack = { navController.popBackStack() }) }
        composable<Screen.FAQCenter> { FAQCenterScreen(onBack = { navController.popBackStack() }) }
        composable<Screen.PrivacyAbout> { PrivacyAboutScreen(onBack = { navController.popBackStack() }) }
    }
}
