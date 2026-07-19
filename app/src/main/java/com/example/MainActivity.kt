package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.AppDatabase
import com.example.data.StudyRepository
import com.example.ui.StudyViewModel
import com.example.ui.StudyViewModelFactory
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NetworkBanner
import com.example.ui.navigation.AppNavGraph
import com.example.ui.navigation.Screen
import com.example.ui.theme.AppTheme
import com.example.ui.utils.NetworkStatus
import com.example.ui.utils.NetworkStatusTracker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val repository = StudyRepository(database.studyDao())
        val factory = StudyViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[StudyViewModel::class.java]
        
        val networkTracker = NetworkStatusTracker(this)
        
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = currentRoute in listOf(
                Screen.Home.route,
                Screen.Chat.route,
                Screen.Translator.route,
                Screen.History.route,
                Screen.Settings.route
            )
            
            val userSettings by viewModel.userSettings.collectAsState()
            val isDarkTheme = userSettings?.darkMode ?: androidx.compose.foundation.isSystemInDarkTheme()
            val networkStatus by networkTracker.networkStatus.collectAsState(initial = NetworkStatus.Available)
            
            AppTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController, lang = userSettings?.language ?: "en")
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AppNavGraph(
                            navController = navController,
                            viewModel = viewModel,
                            innerPadding = innerPadding
                        )
                        Box(modifier = Modifier.align(Alignment.TopCenter).padding(innerPadding)) {
                            NetworkBanner(networkStatus = networkStatus)
                        }
                    }
                }
            }
        }
    }
}
