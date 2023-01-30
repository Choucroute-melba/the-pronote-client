package moi.choucroutemelba.thepronoteclient.ui

import androidx.core.net.toUri
import androidx.navigation.NavHostController
import java.net.URLEncoder

object ThePronoteDestinations {
    const val HOME_ROUTE = "home"
    const val LOGIN_ROUTE = "login/{url}"
}

class ThePronoteNavigation(
    navController: NavHostController
) {
    val navigateToHome: () -> Unit = {
        navController.navigate(ThePronoteDestinations.HOME_ROUTE)
    }
    val navigateToLogin: (url: String) -> Unit = { url ->
        navController.navigate("login/${URLEncoder.encode(url, "UTF-8")}")
    }
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}