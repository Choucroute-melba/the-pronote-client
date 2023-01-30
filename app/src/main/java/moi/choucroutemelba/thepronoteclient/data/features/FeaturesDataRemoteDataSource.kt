package moi.choucroutemelba.thepronoteclient.data.features

import java.util.Date

data class DataUpdateApiModel(
    val lastUpdate: Date,
    val lastEntListUpdate: Date,
    val lastChangelogUpdate: Date,
    val lastInformationUpdate: Date,
)
data class EntApiModel(
    val name: String,
    val region: String,
    val url: String,
)
data class ChangelogApiModel(
    val version: String,
    val date: Date,
    val changes: List<String>,
)

data class InformationApiModel(
    val date: Date,
    val title: String,
    val content: String,
)

interface FeaturesDataRemoteDataSourceApi {
    suspend fun getLastDataUpdate(): Date
    suspend fun getEntList(): List<EntApiModel>
    suspend fun getChangelog(): List<ChangelogApiModel>
    suspend fun getInformation(): List<InformationApiModel>
}

/**
 * This data source is responsible for getting all features relative data like available Ent, changelog, information, etc,
 * currently from the GitHub repository.
 */
class FeaturesDataRemoteDataSource: FeaturesDataRemoteDataSourceApi {
    override suspend fun getLastDataUpdate(): Date {
        TODO("Not yet implemented")
    }
    override suspend fun getEntList(): List<EntApiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChangelog(): List<ChangelogApiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getInformation(): List<InformationApiModel> {
        TODO("Not yet implemented")
    }
}