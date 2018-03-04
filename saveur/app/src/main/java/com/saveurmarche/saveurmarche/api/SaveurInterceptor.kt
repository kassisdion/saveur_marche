package com.saveurmarche.saveurmarche.api

import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SaveurInterceptor(private val preferenceManager: SaveurPreferenceManager) : Interceptor {

    /*
    ************************************************************************************************
    ** Interceptor implementation
    ************************************************************************************************
    */
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addOauthToRequest(chain.request(), preferenceManager.oauthAccessToken))
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun addOauthToRequest(request: Request, accessToken: String?): Request {
        return request.newBuilder()
                .header("Authorization", String.format("Bearer %s", accessToken))
                .build()
    }
}