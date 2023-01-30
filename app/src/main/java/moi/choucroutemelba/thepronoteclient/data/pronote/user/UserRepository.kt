package moi.choucroutemelba.thepronoteclient.data.pronote.user

import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteData

data class Ent(
    val name: String,
    val url: String,
)
data class User(
    val username: String,
    val password: String,
    val useEnt: Boolean,
    val ent: Ent?,
)

class UserRepository (
    private val userRemoteDataSource: UserRemoteDataSource,
){
    suspend fun getTest(url: String): Result<PronoteData> = userRemoteDataSource.getTest(url)
}