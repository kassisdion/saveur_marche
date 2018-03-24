package com.saveurmarche.saveurmarche.api.response

import com.squareup.moshi.Json

data class MarketFileResponse(@Json(name = "serial")
                              val serial: String,
                              @Json(name = "markets")
                              val markets: List<MarketResponse>) {

    data class MarketResponse(@Json(name = "id")
                              val id: String,
                              @Json(name = "description")
                              val description: String? = null,
                              @Json(name = "address")
                              val address: AdressResponse,
                              @Json(name = "web_site_u_r_l")
                              val webSiteUrl: String?,
                              @Json(name = "picture")
                              val picture: String?,
                              @Json(name = "type")
                              val type: String? = null,
                              @Json(name = "exhibitors")
                              val exhibitors: Long? = null,
                              @Json(name = "product_description")
                              val productDescription: String? = null,
                              @Json(name = "name")
                              val name: String? = null)

    data class AdressResponse(@Json(name = "formatted_address")
                              val formatted_address: String,
                              @Json(name = "zip_code")
                              val zip_code: String,
                              @Json(name = "city")
                              val city: String,
                              @Json(name = "country")
                              val country: String,
                              @Json(name = "longitude")
                              val longitude: Double,
                              @Json(name = "latitude")
                              val latitude: Double)
}