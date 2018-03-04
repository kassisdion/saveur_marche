package com.saveurmarche.saveurmarche.api.response

import com.squareup.moshi.Json

class OauthResponse(
        @Json(name = "access_token")
        val accessToken: String,
        @Json(name = "refresh_token")
        val refreshToken: String
)