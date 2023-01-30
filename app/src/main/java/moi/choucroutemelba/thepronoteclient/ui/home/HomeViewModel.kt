package moi.choucroutemelba.thepronoteclient.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteData
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRepository
import moi.choucroutemelba.thepronoteclient.data.pronote.user.Result
import moi.choucroutemelba.thepronoteclient.ui.ThePronoteNavigation


data class HomeUiState(
    val count: Int = 0,
    val fetchJob: Job? = null,
    val response: Result<PronoteData>? = null,
    val urlValue: String = "https://0490003m.index-education.net/pronote/mobile.eleve.html",
)

val HomeUiState.isLoading: Boolean
    get() = fetchJob != null && !fetchJob.isCompleted
val HomeUiState.isError: Boolean
    get() = response is Result.Error
val HomeUiState.isSuccess: Boolean
    get() = response is Result.Success<PronoteData>
val HomeUiState.responseText: String
    get() = (response as Result.Success<PronoteData>).data.data
val HomeUiState.testError: String
    get() = (response as Result.Error).exception.message ?: "Unknown error"
val HomeUiState.fullUrl: String
    get() = urlValue
val HomeUiState.location: String
    get() = (response as Result.Success<PronoteData>).data.origin ?: "Unknown location"


class HomeViewModel(
    private val userRepository: UserRepository,
    private val navAction: ThePronoteNavigation
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private val tag = "home/viewmodel"

    private var fetchJob: Job? = null

    fun increment() {
        uiState = uiState.copy(count = uiState.count + 1)
    }

    fun onUrlChange(url: String) {
        //Log.d(tag, "onUrlChange: $url")
        uiState = uiState.copy(urlValue = url)
    }

    fun getTestSite() {
        navAction.navigateToLogin(uiState.fullUrl)
        /*fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val result = userRepository.getTest(uiState.fullUrl)
            uiState = uiState.copy(response = result)
        }
        fetchJob!!.invokeOnCompletion { Log.i(tag, "Fetchjob completed, ${uiState.isLoading}") }
        uiState = uiState.copy(fetchJob = fetchJob)*/
    }

    companion object {
        fun provideFactory(
            userRepository: UserRepository,
            navAction: ThePronoteNavigation
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(userRepository, navAction) as T
            }
        }
    }
}