package moi.choucroutemelba.thepronoteclient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteData
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRepository
import moi.choucroutemelba.thepronoteclient.data.pronote.user.Result


/*class UiViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var count by mutableStateOf(0)
        private set
    var fetchJob by mutableStateOf<Job?>(null)
        private set
    var testText by mutableStateOf("")
        private set

    fun increment() {
        count++
    }

    fun getTestSite() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val result = userRepository.getTest()
            testText = when (result) {
                is Result.Success<PronoteData> -> result.data.data
                is Result.Error -> result.exception.message ?: "Unknown error"
            }
        }
    }
}*/