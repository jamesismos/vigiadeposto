package br.com.vigiadeposto.ui.navigation

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Evaluate : Screen("evaluate")
    object Ranking : Screen("ranking")
    object Info : Screen("info")
    object AddStation : Screen("add_station")
    object Login : Screen("login")
}
