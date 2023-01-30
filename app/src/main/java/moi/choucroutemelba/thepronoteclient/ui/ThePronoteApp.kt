package moi.choucroutemelba.thepronoteclient.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import moi.choucroutemelba.thepronoteclient.data.AppContainer
import moi.choucroutemelba.thepronoteclient.ui.home.HomeViewModel
import moi.choucroutemelba.thepronoteclient.ui.theme.ThePronoteClientTheme

@Composable
fun ThePronoteApp(
    appContainer: AppContainer
) {
    ThePronoteClientTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            ThePronoteNavigation(navController)
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: ThePronoteDestinations.HOME_ROUTE


        Surface(color = MaterialTheme.colors.background) {
            ThePronoteNavGraph(
                appContainer = appContainer,
                navController = navController,
                navigation = navigationActions,
            )
        }
    }
}