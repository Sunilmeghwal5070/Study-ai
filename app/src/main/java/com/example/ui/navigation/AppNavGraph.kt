package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.StudyViewModel
import com.example.ui.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Setup : Screen("setup")
    object Home : Screen("home")
    object Solver : Screen("solver/{subjectId}/{subjectName}") {
        fun createRoute(id: String, name: String) = "solver/$id/$name"
    }
    object Chat : Screen("chat")
    object Translator : Screen("translator")
    object History : Screen("history")
    object Settings : Screen("settings")
    object About : Screen("about")
    object Privacy : Screen("privacy")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: StudyViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400)) }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Setup.route) {
            SetupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel, innerPadding = innerPadding)
        }
        composable(
            route = Screen.Solver.route,
            arguments = listOf(
                navArgument("subjectId") { type = NavType.StringType },
                navArgument("subjectName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: "math"
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: "Math"
            SolverScreen(
                navController = navController,
                viewModel = viewModel,
                subjectId = subjectId,
                subjectName = subjectName,
                innerPadding = innerPadding
            )
        }
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController, viewModel = viewModel, innerPadding = innerPadding)
        }
        composable(Screen.Translator.route) {
            TranslatorScreen(navController = navController, viewModel = viewModel, innerPadding = innerPadding)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController = navController, viewModel = viewModel, innerPadding = innerPadding)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = viewModel, innerPadding = innerPadding)
        }
        composable(Screen.About.route) {
            AboutScreen(navController = navController, innerPadding = innerPadding)
        }
        composable(Screen.Privacy.route) {
            PrivacyScreen(navController = navController, innerPadding = innerPadding)
        }
    }
}
