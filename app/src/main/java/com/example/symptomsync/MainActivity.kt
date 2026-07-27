package com.example.symptomsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.symptomsync.navigation.Screen
import com.example.symptomsync.navigation.SymptomSyncNavHost
import com.example.symptomsync.ui.theme.*
import com.example.symptomsync.ui.theme.SymptomsyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SymptomsyncTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (showBottomBar(currentRoute)) {
                            BottomBar(currentRoute = currentRoute) { screen: Any ->
                                navController.navigate(screen) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                ) { innerPadding: PaddingValues ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        SymptomSyncNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(currentRoute: String?, onNavigate: (Any) -> Unit) {
    NavigationBar(
        containerColor = if ((currentRoute?.contains("CoreHome") == true) || (currentRoute?.contains("DailyDietPlan") == true)) WellnessDarkCharcoal else WellnessWarmWhite,
        contentColor = if ((currentRoute?.contains("CoreHome") == true) || (currentRoute?.contains("DailyDietPlan") == true)) WellnessWarmWhite else WellnessDarkNavy,
    ) {
        val isDark = currentRoute?.contains("CoreHome") == true || currentRoute?.contains("DailyDietPlan") == true
        val selectedColor = if (isDark) RichOrange else SmoothPurple
        val unselectedColor = if (isDark) WellnessWarmWhite.copy(alpha = 0.5f) else WellnessDarkNavy.copy(alpha = 0.5f)

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute?.contains("CoreHome") == true || currentRoute?.contains("Home") == true,
            onClick = { onNavigate(Screen.CoreHome) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Activity") },
            label = { Text("Activity") },
            selected = currentRoute?.contains("AnalyticsHub") == true,
            onClick = { onNavigate(Screen.AnalyticsHub) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            selected = currentRoute?.contains("HistoryLog") == true,
            onClick = { onNavigate(Screen.HistoryLog) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute?.contains("UserProfile") == true,
            onClick = { onNavigate(Screen.UserProfile) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )
    }
}

fun showBottomBar(route: String?): Boolean {
    val nonBottomBarScreens = listOf("Splash", "Login", "Register", "BasicInfo", "DietaryPreferences", "FitnessGoals", "SymptomSelection", "SeverityScale", "ActivityLevel", "AIProcessing", "ProfileSuccess", "CoreSymptomSelect", "CoreAnalysis", "CoreCauses", "CoreDietPlan", "CoreWaterTracker", "CoreProgress", "CoreHistory")
    return route == null || !nonBottomBarScreens.any { route.contains(it) }
}
