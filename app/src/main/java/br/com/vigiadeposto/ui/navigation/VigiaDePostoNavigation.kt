package br.com.vigiadeposto.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.vigiadeposto.ui.screens.add.AddStationScreen
import br.com.vigiadeposto.ui.screens.auth.LoginScreen
import br.com.vigiadeposto.ui.screens.evaluate.EvaluateScreen
import br.com.vigiadeposto.ui.screens.info.InfoScreen
import br.com.vigiadeposto.ui.screens.map.MapScreen
import br.com.vigiadeposto.ui.screens.ranking.RankingScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Map.route) {
            MapScreen(
                onNavigateToAddStation = {
                    navController.navigate(Screen.AddStation.route)
                },
                onNavigateToStationDetails = { stationId ->
                    // Navigate to station details when implemented
                }
            )
        }
        
        composable(Screen.Evaluate.route) {
            EvaluateScreen(
                onNavigateToStationDetails = { stationId ->
                    // Navigate to station details when implemented
                }
            )
        }
        
        composable(Screen.Ranking.route) {
            RankingScreen(
                onNavigateToStationDetails = { stationId ->
                    // Navigate to station details when implemented
                }
            )
        }
        
        composable(Screen.Info.route) {
            InfoScreen()
        }
        
        composable(Screen.AddStation.route) {
            AddStationScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
