package moi.choucroutemelba.thepronoteclient.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import moi.choucroutemelba.thepronoteclient.data.AppContainer
import moi.choucroutemelba.thepronoteclient.ui.home.HomeRoute
import moi.choucroutemelba.thepronoteclient.ui.home.HomeViewModel
import moi.choucroutemelba.thepronoteclient.ui.login.LoginRoute
import moi.choucroutemelba.thepronoteclient.ui.login.LoginViewModel

@Composable
fun ThePronoteNavGraph(
    appContainer: AppContainer,
    navController: NavHostController,
    navigation: ThePronoteNavigation
) {
    NavHost(
        navController = navController,
        startDestination = ThePronoteDestinations.HOME_ROUTE
    ) {
        composable(ThePronoteDestinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.userRepository, navigation)
            )
            HomeRoute(
                homeViewModel = homeViewModel
            )
        }
        composable(
            ThePronoteDestinations.LOGIN_ROUTE,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val args: Map<String, String?> = mapOf(Pair("url", backStackEntry.arguments?.getString("url")))
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.provideFactory(appContainer.userRepository,
                    appContainer.featuresDataRepository,
                    navigation, args)
            )
            LoginRoute(
                loginViewModel = loginViewModel
            )
        }
    }
}