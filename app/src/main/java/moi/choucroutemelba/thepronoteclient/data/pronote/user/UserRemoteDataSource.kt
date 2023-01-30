package moi.choucroutemelba.thepronoteclient.data.pronote.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteApi
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteData

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class UserRemoteDataSource (
    private val pronoteApi: PronoteApi,
    private val ioDispatcher: CoroutineDispatcher
    ){
    suspend fun getTest(url: String): Result<PronoteData> {
        return withContext(ioDispatcher) {
            try {
                return@withContext Result.Success(pronoteApi.getTest(url))
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
    }
}