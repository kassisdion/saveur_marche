package com.saveurmarche.saveurmarche.api.response

import com.squareup.moshi.Json

data class MarketsUrlResponse(
        @Json(name = "url")
        val url: String
)