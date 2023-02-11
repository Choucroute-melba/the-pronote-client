package moi.choucroutemelba.thepronoteclient.data.features

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date

data class DataUpdateApiModel(
    val lastUpdate: String,
    val lastEntListUpdate: String,
    val lastChangelogUpdate: String,
    val lastInformationUpdate: String,
)
data class EntApiModel(
    val name: String,
    val region: String,
    val url: String,
)
data class EntListApiModel(
    val last_updated: String,
    val list: List<EntApiModel>
)

data class ChangelogApiModel(
    val version: String,
    val date: Date,
    val changes: List<String>,
)
data class ChangelogListApiModel(
    val lastUpdate: String,
    val changelogList: List<ChangelogApiModel>
)

data class EstablishmentApiModel(
    val url: String,
    val nomEtab: String,
    val lat: String,
    val long: String,
    val cp: String
)
data class EstablishmentListApiModel(
    val lastUpdate: String,
    val establishmentList: List<EstablishmentApiModel>
)

data class InformationApiModel(
    val date: Date,
    val title: String,
    val content: String,
)
data class InformationListApiModel(
    val lastUpdate: String,
    val informationList: List<InformationApiModel>
)

interface FeaturesDataRemoteDataSourceApi {
    suspend fun getLastDataUpdate(): DataUpdateApiModel
    suspend fun getEntList(): EntListApiModel
    suspend fun getEstablishmentList(postalCode: String): EstablishmentListApiModel // TODO: Add a way to get the list of every establishment
    suspend fun getChangelog(): ChangelogListApiModel
    suspend fun getInformation(): InformationListApiModel
}

/**
 * This data source is responsible for getting all features relative data like available Ent, changelog, information, etc,
 * currently from the GitHub repository.
 */
class FeaturesDataRemoteDataSource(
    private val featuresDataApi: FeaturesDataApi,
    private val ioDispatcher: CoroutineDispatcher
): FeaturesDataRemoteDataSourceApi {
    override suspend fun getLastDataUpdate(): DataUpdateApiModel {
        return withContext(ioDispatcher) {
            return@withContext featuresDataApi.getLastDataUpdate()
        }
    }
    override suspend fun getEntList(): EntListApiModel {
        val data = withContext(ioDispatcher) {
            return@withContext featuresDataApi.getEntList()
        }
        Log.i("FeaturesDataRemoteDataSource", data.toString())
        return data
    }

    override suspend fun getEstablishmentList(postalCode: String): EstablishmentListApiModel {
        return withContext(ioDispatcher) {
            val localisationData: LocalisationApiModel = featuresDataApi.getLocalisationInformation(postalCode) as LocalisationApiModel
            Log.i("FeaturesDataRemoteDataSource", localisationData.toString())
            if(localisationData.latitude == 0.0 || localisationData.longitude == 0.0) {
                throw Exception("No establishment found for the postal code $postalCode")
            }
            return@withContext featuresDataApi.getEstablishmentList(localisationData.latitude.toString(), localisationData.longitude.toString())
        }
    }

    override suspend fun getChangelog(): ChangelogListApiModel {
        TODO("Not yet implemented")
    }

    override suspend fun getInformation(): InformationListApiModel {
        TODO("Not yet implemented")
    }
}