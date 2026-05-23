package br.com.vigiadeposto.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.vigiadeposto.ui.navigation.NavGraph
import br.com.vigiadeposto.ui.navigation.Screen
import br.com.vigiadeposto.ui.theme.VigiaDePostoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VigiaDePostoTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    // App principal com bottom navigation (sem autenticação obrigatória)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "VIGIA DE POSTO",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    // Botão de login opcional no canto superior direito
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Login.route)
                        }
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Login Opcional",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentDestination?.route
                
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Mapa") },
                    label = { Text("Mapa") },
                    selected = currentRoute == Screen.Map.route,
                    onClick = {
                        navController.navigate(Screen.Map.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, "Avaliar") },
                    label = { Text("Avaliar") },
                    selected = currentRoute == Screen.Evaluate.route,
                    onClick = {
                        navController.navigate(Screen.Evaluate.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, "Ranking") },
                    label = { Text("Ranking") },
                    selected = currentRoute == Screen.Ranking.route,
                    onClick = {
                        navController.navigate(Screen.Ranking.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, "Info") },
                    label = { Text("Info") },
                    selected = currentRoute == Screen.Info.route,
                    onClick = {
                        navController.navigate(Screen.Info.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            startDestination = Screen.Map.route
        )
    }
}
