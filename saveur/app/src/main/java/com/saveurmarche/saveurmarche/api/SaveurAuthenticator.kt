package com.saveurmarche.saveurmarche.api

import com.saveurmarche.saveurmarche.BuildConfig
import com.saveurmarche.saveurmarche.api.response.OauthResponse
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import com.saveurmarche.saveurmarche.helper.logE
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class SaveurAuthenticator(private val service: SaveurServices.OauthService,
                          private val preferenceManager: SaveurPreferenceManager) : Authenticator {
    private val TAG: String = SaveurAuthenticator::class.java.name

    /*
    ************************************************************************************************
    ** Authenticator implementation
    ************************************************************************************************
    */
    @Throws(IOException::class)
    override fun authenticate(route: Route,
                              originalResponse: Response): Request? {
        if (responseCount(originalResponse) >= 2) {
            // If both the original call and the call with refreshed token failed,
            // it will probably keep failing, so don't try again.
            return null
        }

        try {
            val oauthResponse = service
                    .login(BuildConfig.GRANT_TYPE_REFRESH, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, BuildConfig.SCOPE_USER)
                    .execute()
            if (oauthResponse.code() == 200) {
                val oauthResponseBody = oauthResponse.body()
                if (oauthResponseBody != null) {
                    onOauthSuccess(oauthResponseBody)
                    return addOauthToRequest(originalResponse.request(), preferenceManager.oauthAccessToken)
                }
            }

        } catch (e: IOException) {
            logE(TAG, { "authenticate > fail" }, e)
        }

        return null
    }

    /*
    ************************************************************************************************
    ** Private method
    ************************************************************************************************
    */
    private fun responseCount(originalResponse: Response): Int {
        var result = 0
        var response: Response? = originalResponse

        while (response != null) {
            response = response.priorResponse()
            result++
        }
        return result
    }

    private fun onOauthSuccess(oauthResponseBody: OauthResponse) {
        preferenceManager.oauthLoggedIn = true
        preferenceManager.oauthAccessToken = oauthResponseBody.accessToken
        preferenceManager.oauthRefreshToken = oauthResponseBody.refreshToken
    }

    private fun addOauthToRequest(request: Request, accessToken: String?): Request {
        return request.newBuilder()
                .header("Authorization", String.format("Bearer %s", accessToken))
                .build()
    }
}

