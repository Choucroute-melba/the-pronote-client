package moi.choucroutemelba.thepronoteclient.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import moi.choucroutemelba.thepronoteclient.data.features.FeaturesDataApi
import moi.choucroutemelba.thepronoteclient.data.features.FeaturesDataApiImpl
import moi.choucroutemelba.thepronoteclient.data.features.FeaturesDataRemoteDataSource
import moi.choucroutemelba.thepronoteclient.data.features.FeaturesDataRepository
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteApi
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRemoteDataSource
import moi.choucroutemelba.thepronoteclient.data.pronote.user.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val featuresDataRepository: FeaturesDataRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    private val pronoteApi = PronoteApi
    private val featuresDataApi: FeaturesDataApi = FeaturesDataApiImpl

    private val userRemoteDataSource: UserRemoteDataSource by lazy {
        UserRemoteDataSource(pronoteApi, Dispatchers.IO)
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(userRemoteDataSource)
    }

    private val featuresDataRemoteDataSource: FeaturesDataRemoteDataSource by lazy {
        FeaturesDataRemoteDataSource(featuresDataApi, Dispatchers.IO)
    }

    override val featuresDataRepository: FeaturesDataRepository by lazy {
        FeaturesDataRepository(featuresDataRemoteDataSource)
    }
}