package com.example.symptomsync

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.symptomsync.navigation.SymptomSyncNavHost
import com.example.symptomsync.ui.theme.SymptomsyncTheme

@Composable
fun App() {
    SymptomsyncTheme {
        val navController = rememberNavController()
        SymptomSyncNavHost(navController = navController)
    }
}

