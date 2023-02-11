package moi.choucroutemelba.thepronoteclient.data.features

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.*
import moi.choucroutemelba.thepronoteclient.data.pronote.user.Ent
import java.time.Instant
import java.util.*

enum class ApiErrorType {
    NO_INTERNET,
    HTTP_ERROR,
    BAD_RESPONSE,
    UNKNOWN_ERROR,
}
data class ApiError(
    val type: ApiErrorType,
    override val message: String,
    val e: Exception? = null,
): Exception() {
    constructor(e: Exception) : this(
        ApiErrorType.UNKNOWN_ERROR,
        e.message ?: "An error occurred"
    )
}
data class DataUpdate(
    val lastUpdate: Date,
    val lastEntListUpdate: Date,
    val lastChangelogUpdate: Date,
    val lastInformationUpdate: Date,
)

data class Establishment(
    val url: String,
    val name: String,
    val cp: String,
)

/**
 * Repository that exposes all features relative data that may require to be regularly updated from the server.
 */
class FeaturesDataRepository(
    private val featuresDataRemoteDataSource: FeaturesDataRemoteDataSource
) {
    private val _error = MutableStateFlow<ApiError?>(null)
    val error: Flow<ApiError> = _error.filterNotNull()
    suspend fun getEntList(): List<Ent> =
        try {
            featuresDataRemoteDataSource.getEntList().list.map { Ent(it.name, it.url) }
        } catch (e: HttpException) {
            Log.e("FeaturesDataRepository", e.stackTraceToString())
            val err = ApiError(
                type = ApiErrorType.HTTP_ERROR,
                message = e.message ?: "An HTTP error occurred",
                e = e
            )
            _error.value = err
            throw err
        } catch (e: Exception) {
            Log.e("FeaturesDataRepository", "getList: ${e.message}}")
            Log.e("FeaturesDataRepository", e.stackTraceToString())
            val err = ApiError(
                ApiErrorType.UNKNOWN_ERROR,
                "An error occurred - ${e.message}",
                e = e
            )
            _error.value = err
            throw err
        }

    suspend fun getEstablishmentList(postalCode: String): List<Establishment> =
        try {
            featuresDataRemoteDataSource.getEstablishmentList(postalCode).establishmentList
                .map { Establishment(it.url, it.nomEtab, it.cp) }
        } catch (e: HttpException) {
            Log.e("FeaturesDataRepository", e.stackTraceToString())
            val err = ApiError(
                ApiErrorType.HTTP_ERROR,
                "An HTTP error occurred - ${e.message}",
                e = e
            )
            _error.value = err
            throw err
        } catch (e: JsonSyntaxException) {
            Log.e("FeaturesDataRepository", e.stackTraceToString())
            val err = ApiError(
                ApiErrorType.BAD_RESPONSE,
                "A JSON error occurred - ${e.message}\n ${e.cause?.message}",
                e = e
            )
            _error.value = err
            throw err
        } catch (e: Exception) {
            Log.e("FeaturesDataRepository", "getEstablishmentList: ${e.message}}")
            Log.e("FeaturesDataRepository", e.stackTraceToString())
            val err = ApiError(
                ApiErrorType.UNKNOWN_ERROR,
                "An error occurred - ${e.message}",
                e = e
            )
            _error.value = err
            throw err
        }

    suspend fun getUpdatesDates(): DataUpdate {
        lateinit var dataUpdate: DataUpdate
        try {
            val dataUpdateApiModel = featuresDataRemoteDataSource.getLastDataUpdate()
            dataUpdate = DataUpdate(
                Date.from(Instant.parse(dataUpdateApiModel.lastUpdate)),
                Date.from(Instant.parse(dataUpdateApiModel.lastEntListUpdate)),
                Date.from(Instant.parse(dataUpdateApiModel.lastChangelogUpdate)),
                Date.from(Instant.parse(dataUpdateApiModel.lastInformationUpdate))
            )
        } catch (e: HttpException) {
            val err = ApiError(
                ApiErrorType.HTTP_ERROR,
                e.message ?: "An HTTP error occurred",
                e = e
            )
            _error.value = err
        } catch (e: Exception) {
            val err = ApiError(
                ApiErrorType.UNKNOWN_ERROR,
                "An error occurred - ${e.message}",
                e = e
            )
            _error.value = err
        }
        return dataUpdate
    }
}