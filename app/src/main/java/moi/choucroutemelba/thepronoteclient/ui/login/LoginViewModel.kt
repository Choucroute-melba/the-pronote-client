package moi.choucroutemelba.thepronoteclient.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moi.choucroutemelba.thepronoteclient.data.features.ApiError
import moi.choucroutemelba.thepronoteclient.data.features.Establishment
import moi.choucroutemelba.thepronoteclient.data.features.FeaturesDataRepository
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRepository
import moi.choucroutemelba.thepronoteclient.ui.ThePronoteNavigation
import kotlin.coroutines.cancellation.CancellationException

enum class ConnectionSteps {
    SELECT_ESTABLISHMENT,
    CONNECT
}

enum class SelectionMethod {
    UNKNOWN,
    URL,
    GEOLOCATION,
}

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
    var establishments: Map<String, Establishment> = mapOf(),
    var postalCode: String? = null,
    var connectionSteps: ConnectionSteps = ConnectionSteps.SELECT_ESTABLISHMENT,
    var selectionMethod: SelectionMethod = SelectionMethod.GEOLOCATION,
    var useEnt: Boolean = false,
    var entName: String? = null,
    var username: String = "",
    var password: String = "",
    var isLoading: Boolean = false,
    var loadingEstablishments: Boolean = false,
    val loadingEntList: Boolean = false,
    var error: ApiError? = null
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val featuresDataRepository: FeaturesDataRepository,
    private val navAction: ThePronoteNavigation,
    private val arguments: Map<String, String?>
): ViewModel() {
    var uiState by mutableStateOf(LoginUiState(
        url = arguments["url"]
    ))
        private set

    private var fetchEntListJob: Job? = null
    private var fetchEstablishmentListJob: Job? = null
    init {
        viewModelScope.launch {
            featuresDataRepository.error.collect {
                uiState = uiState.copy(error = it)
            }
        }
        getEntList()
    }

    private fun getEntList() {
        uiState = uiState.copy(isLoading = true, loadingEntList = true)
        uiState = uiState.copy(error = null)
        fetchEntListJob = viewModelScope.launch {
            try {
                val entList = featuresDataRepository.getEntList()
                uiState = uiState.copy(availableEnt = entList.associate { it.url to it.name })
            } catch (e: ApiError) {
                fetchEntListJob?.cancel(CancellationException(e))
            } catch (e: Exception) {
                fetchEntListJob?.cancel(CancellationException(e))
            }
        }
        fetchEntListJob?.invokeOnCompletion {
            uiState = uiState.copy(loadingEntList = false)
            onCompletion(it)
        }
    }

    fun getEstablishmentList() {
        uiState = uiState.copy(isLoading = true, loadingEstablishments = true)
        uiState = uiState.copy(error = null)
        fetchEstablishmentListJob = viewModelScope.launch {
            try {
                if(uiState.postalCode != null)
                    featuresDataRepository.getEstablishmentList(uiState.postalCode!!).apply {
                        uiState = uiState.copy(establishments = this.associateBy { it.url })
                    }
                else fetchEstablishmentListJob?.cancel(CancellationException( ApiError(Exception("Postal code is null")) ))
            } catch (e: ApiError) {
                fetchEstablishmentListJob?.cancel(CancellationException(e))
            } catch (e: Exception) {
                fetchEstablishmentListJob?.cancel(CancellationException(e))
            }
        }
        fetchEstablishmentListJob?.invokeOnCompletion {
            uiState = uiState.copy(loadingEstablishments = false)
            onCompletion(it)
        }
    }

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

    fun setPostalCode(postalCode: String) {
        uiState = uiState.copy(postalCode = postalCode)
    }

    fun setSelectionMethod(selectionMethod: SelectionMethod) {
        uiState = uiState.copy(selectionMethod = selectionMethod)
    }

    private fun onCompletion(result: Throwable? ) {
        uiState = uiState.copy(isLoading = false)
        if(result != null) {
            when (result) {
                is CancellationException -> {
                    uiState = uiState.copy(error = result.cause as ApiError)
                }
                is ApiError -> {
                    uiState = uiState.copy(error = result)
                }
                is Exception -> {
                    uiState = uiState.copy(error = ApiError(result))
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            userRepository: UserRepository,
            featuresDataRepository: FeaturesDataRepository,
            navAction: ThePronoteNavigation,
            arguments: Map<String, String?>
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userRepository, featuresDataRepository, navAction, arguments) as T
            }
        }
    }
}