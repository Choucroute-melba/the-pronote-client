package moi.choucroutemelba.thepronoteclient.data.pronote.api

import android.util.Log
import com.google.gson.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val tag = "data/pronote/api"


data class PronoteData(
    val data: String,
    val origin: String? = null,
)

fun readStream(input: InputStream): String {
    val ir = InputStreamReader(input, "UTF-8")
    val sb = StringBuilder()
    val buf = CharArray(1024)
    var n: Int
    while (ir.read(buf).also { n = it } != -1) {
        sb.append(buf, 0, n)
    }
    return sb.toString()
}

object PronoteApi {
    private const val BASE_URL = "https://android.com/"
    suspend fun getTest(urlV: String): PronoteData {
        var input: String? = null
        var headers: MutableMap<String?, MutableList<String?>?>? = null
        var error: String? = null
        var code: Int = 300
        var redirectionsList: List<String> = listOf(urlV)



        while(code in 300..399) {
            val url = URL(redirectionsList.last())
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            Log.i("$tag/req", "getTest: $url")
            con.requestMethod = "GET"
            con.instanceFollowRedirects = false
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0")
            con.connect()
            Log.i("$tag/req", "${con.requestMethod} ${con.url} ${con.responseCode}")
            code = con.responseCode
            headers = con.headerFields
            if(code in 300..399) {
                val location = headers["Location"]?.first()
                if(location != null)
                    redirectionsList = redirectionsList + location
                continue
            }
            if(code in 200..299) {
                try {
                    input = readStream(con.inputStream)
                } catch (e: Exception) {
                    error = e.message
                    Log.e("$tag/req/error", error ?: "Unknown error")
                    throw e
                }
            } else {
                error = readStream(con.errorStream)
                Log.i("$tag/req/error", "HTTP Error : $error")
                throw Exception("Error $code: $error")
            }
        }
        Log.i("$tag/req", "final url: ${redirectionsList.last()}")
        return PronoteData(input!!, redirectionsList.last())
    }
}

