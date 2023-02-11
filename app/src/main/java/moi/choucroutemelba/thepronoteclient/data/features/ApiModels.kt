package moi.choucroutemelba.thepronoteclient.data.features

import java.util.ArrayList

data class LocalisationDataApiModel(
    val data: ArrayList<Any>
)

/**
 * This data class represent an answer from the https://positionstack.com/geo_api.php?query=france+{postal_code} API endpoint.
 */
data class LocalisationApiModel(
    val latitude: Double,
    val longitude: Double,
    val type: String? = null,
    val name: String? = null,
    val number: String? = null,
    val postal_code: String? = null,
    val street: String? = null,
    val confidence: Int? = null,
    val region: String? = null,
    val region_code: String? = null,
    val county: String? = null,
    val locality: String? = null,
    val administrative_area: String? = null,
    val neighbourhood: String? = null,
    val country: String? = null,
    val country_code: String? = null,
    val continent: String? = null,
    val label: String? = null,
    val bbox_module: Any? = null,
    val country_module: Any? = null,
    val sun_module: Any? = null,
    val timezone_module: Any? = null,
)

data class CountryModuleApiModel(
    val latitude: Double,
    val longitude: Double,
    val common_name: String,
    val official_name: String,
    val capital: String,
    val flag: String,
    val area: Int,
    val landlocked: Boolean,
    val independent: Boolean,
    val global: GlobalApiModel,
    val dial: DialApiModel,
    val currencies: List<CurrencyApiModel>,
    val languages: List<LanguageApiModel>,
)
data class GlobalApiModel(
    val alpha2: String,
    val alpha3: String,
    val numeric_code: String,
    val region: String,
    val subregion: String,
    val region_code: String,
    val subregion_code: String,
    val world_region: String,
    val continent_name: String,
    val continent_code: String,
)
data class DialApiModel(
    val calling_code: String,
    val national_prefix: String,
    val international_prefix: String,
)
data class CurrencyApiModel(
    val symbol: String,
    val code: String,
    val name: String,
    val numeric: Int,
    val minor_unit: Int
)
data class LanguageApiModel(
    val name: String
)