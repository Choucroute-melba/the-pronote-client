package moi.choucroutemelba.thepronoteclient.data.features

import android.util.Log
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HttpException(
    val code: Int,
    val codeMessage: String,
    val serverMessage: String? = null,
    override val message: String? = null,
    val connection: HttpURLConnection? = null,
): Exception()

interface FeaturesDataApi {
    fun getLastDataUpdate(): DataUpdateApiModel
    fun getEntList(): EntListApiModel
    fun getEstablishmentList(): EstablishmentListApiModel
    fun getLocalisationInformation(postalCode: String): Any
    fun getEstablishmentList(latitude: String, longitude: String): EstablishmentListApiModel
}

object FeaturesDataApiImpl: FeaturesDataApi {
    private const val TAG = "featuresData/api"

    /**
     * Get the last update dates of all features data by making a request to https://raw.githubusercontent.com/Choucroute-melba/the-pronote-client/master/data/updates.json
     *
     * @return [DataUpdateApiModel] The last update dates of all features data.
     */
    override fun getLastDataUpdate(): DataUpdateApiModel {
        val url = URL("https://raw.githubusercontent.com/Choucroute-melba/the-pronote-client/master/data/updates.json")
        Log.i(TAG, "getLastDataUpdate: url = $url")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val result = reader.readText()
            reader.close()
            inputStream.close()
            Log.i(TAG, result)
            return Gson().fromJson(result, DataUpdateApiModel::class.java)
        } else {
            throw HttpException(responseCode, connection.responseMessage)
        }
    }

    /**
     * Get the list of all available ENT by making a request to https://raw.githubusercontent.com/Choucroute-melba/the-pronote-client/master/data/list.json
     *
     * @return [EntListApiModel] The list of all available ENT.
     */
    override fun getEntList(): EntListApiModel {
        val url = URL("https://raw.githubusercontent.com/Choucroute-melba/the-pronote-client/master/data/entList.json")
        Log.i(TAG, "getList: url = $url")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val result = reader.readText()
            reader.close()
            inputStream.close()
            Log.i(TAG, result)
            return Gson().fromJson(result, EntListApiModel::class.java)
        } else {
            throw HttpException(responseCode, connection.responseMessage)
        }
    }

    /**
     * Get the list of all available establishments by making a request to www.index-education.com/swie/geolocation.php
     *
     * @return [EstablishmentListApiModel] The list of all available establishments.
     */
    override fun getEstablishmentList(): EstablishmentListApiModel {
        TODO("Not yet implemented")
    }

    /**
     * Get the list of available establishments for the selected location by making a POST request to
     * www.index-education.com/swie/geolocation.php with the following parameters:
     * - nomFonction: geoLoc
     * - lat: [latitude]
     * - long: [longitude]
     *
     * @param latitude Where the establishment should be.
     * @param longitude Where the establishment should be.
     * @return [EstablishmentListApiModel] The list of all available establishments.
     */
    override fun getEstablishmentList(latitude: String, longitude: String): EstablishmentListApiModel {
        val url = URL("https://www.index-education.com/swie/geoloc.php")
        Log.i(TAG, "getEstablishmentList: url = $url?nomFonction=geoLoc&lat=$latitude&long=$longitude")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/110.0")
        connection.setRequestProperty("Accept", "*/*")
        // connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8")
/*
        connection.setRequestProperty("Sec-Fetch-Dest", "empty")
        connection.setRequestProperty("Sec-Fetch-Mode", "cors")
        connection.setRequestProperty("Sec-Fetch-Site", "cross-site")
*/

/*
        connection.setRequestProperty("Referer", "https://v4.pronote.plus/")
        connection.setRequestProperty("Pragma", "no-cache")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.setRequestProperty("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/110.0")
*/
        connection.connectTimeout = 5000
        connection.readTimeout = 10*1000
        connection.doOutput = true
        connection.outputStream.write(("data=" + URLEncoder.encode("{\"nomFonction\":\"geoLoc\",\"lat\":\"$latitude\",\"long\":\"$longitude\"}", "UTF-8")).toByteArray())
        Log.i(TAG, "data=" + URLEncoder.encode("{\"nomFonction\":\"geoLoc\",\"lat\":\"$latitude\",\"long\":\"$longitude\"}", "UTF-8"))
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val result = "{\"lastUpdate\": \"peekaboo\", \n\"establishmentList\": " + reader.readText() + "}"
            reader.close()
            inputStream.close()
            Log.i(TAG, result)
            val resultObj = Gson().fromJson(result, EstablishmentListApiModel::class.java)
            Log.i(TAG, resultObj.toString())
            return resultObj
        } else {
            throw HttpException(
                responseCode,
                connection.responseMessage ?: "Server returned an error.",
                connection.errorStream.readBytes().toString(Charsets.UTF_8),
                "$url - data={\"nomFonction\":\"geoLoc\",\"lat\":\"$latitude\",\"long\":\"$longitude\"}",
                connection)
        }
    }

    /**
     * Get the localisation information from https://positionstack.com/geo_api.php?query=france+[postalCode]
     *
     * @param postalCode The postal code of the user.
     * @return [LocalisationApiModel] The localisation information.
     */
    override fun getLocalisationInformation(postalCode: String): Any {
        val url = URL("https://positionstack.com/geo_api.php?query=france+$postalCode")
        Log.i(TAG, "getLocalisationInformation: url = $url")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val result = reader.readText()
            reader.close()
            inputStream.close()
            Log.i(TAG, result)
            val rawData = Gson().fromJson(result, LocalisationDataApiModel::class.java).data[0] as LinkedTreeMap<*, *>
            Log.i(TAG, rawData.toString())
            return LocalisationApiModel(
                latitude = rawData["latitude"] as Double,
                longitude = rawData["longitude"] as Double,
                country = rawData["country"] as String,
                region = rawData["region"] as String,
                type = rawData["type"] as String,
                county = rawData["county"] as String,
                label = rawData["label"] as String,
            )
        } else {
            throw HttpException(responseCode, connection.responseMessage,
                connection.errorStream.readBytes().toString(Charsets.UTF_8),
                url.toString())
        }
    }
}