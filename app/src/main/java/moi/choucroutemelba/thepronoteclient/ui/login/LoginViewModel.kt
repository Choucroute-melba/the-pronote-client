package moi.choucroutemelba.thepronoteclient.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRepository
import moi.choucroutemelba.thepronoteclient.ui.ThePronoteNavigation

data class LoginUiState(
    val url: String? = null,
    val availableEnt: Map<String, String> = mapOf(
        "https://cas3.e-lyco.fr/" to "e-lyco",
        "https://cas.ent.auvergnerhonealpes.fr/" to "Auvergne-Rhône-Alpes",
        "https://cas.ent.bourgognefranchecomte.fr/" to "Bourgogne-Franche-Comté",
        "https://cas.ent.bretagne.fr/" to "Bretagne",
        "https://cas.ent.centrevaldeloire.fr/" to "Centre-Val de Loire",
        "https://cas.ent.corse.fr/" to "Corse",
        "https://cas.ent.guyane.fr/" to "Guyane",
        "https://cas.ent.hautsdefrance.fr/" to "Hauts-de-France",
        "https://cas.ent.iledefrance.fr/" to "Île-de-France",
    ),
    var useEnt: Boolean = false,
    var entName: String? = null,
    var username: String = "",
    var password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val navAction: ThePronoteNavigation,
    private val arguments: Map<String, String?>
): ViewModel() {
    var uiState by mutableStateOf(LoginUiState(
        url = arguments["url"]
    ))
        private set

    fun navigateToHome() {
        navAction.navigateToHome()
    }

    fun navigateBack() {
        navAction.navigateBack()
    }

    fun setUseEnt(useEnt: Boolean) {
        uiState = uiState.copy(useEnt = useEnt)
    }
    fun setEntName(name: String) {
        uiState = uiState.copy(entName = name)
    }

    fun setUsername(username: String) {
        uiState = uiState.copy(username = username)
    }

    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    companion object {
        fun provideFactory(
            userRepository: UserRepository,
            navAction: ThePronoteNavigation,
            arguments: Map<String, String?>
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userRepository, navAction, arguments) as T
            }
        }
    }
}