package com.saveurmarche.saveurmarche.api

import com.saveurmarche.saveurmarche.api.response.MarketsUrlResponse
import com.saveurmarche.saveurmarche.api.response.OauthResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface SaveurServices {
    companion object {
        const val GET_MARKETS_URL = "/api/markets"
        const val OAUTH_URL = "/oauth/v2/token"
    }

    interface ApiService {
        @GET(GET_MARKETS_URL)
        fun getMarketUrl(@Query("since") since: Int?): Single<MarketsUrlResponse>
    }


    interface OauthService {
        @FormUrlEncoded
        @POST(OAUTH_URL)
        fun login(@Field("grant_type") grantType: String,
                  @Field("client_id") clientId: String,
                  @Field("client_secret") clientSecret: String,
                  @Field("scope") scope: String): Call<OauthResponse>
    }
}