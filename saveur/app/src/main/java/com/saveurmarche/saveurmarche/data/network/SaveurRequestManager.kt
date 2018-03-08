package com.saveurmarche.saveurmarche.data.network

import com.saveurmarche.saveurmarche.api.Services
import com.saveurmarche.saveurmarche.api.response.MarketFileResponse
import com.saveurmarche.saveurmarche.api.response.MarketsUrlResponse
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import io.reactivex.Single
import retrofit2.Response

class SaveurRequestManager(private val service: Services.ApiService,
                           private val preferenceManager: SaveurPreferenceManager) {
    fun getMarketUrl(): Single<Response<MarketsUrlResponse>> {
        val oauthHeader = String.format("Bearer %s", preferenceManager.oauthAccessToken)

        return service
                .getMarketsUrl(oauthHeader, preferenceManager.lastJsonFetchData)
                .doOnSuccess {
                    preferenceManager.lastJsonFetchData = System.currentTimeMillis() / 1000
                }
    }

    fun getMarketsFile(url: String): Single<MarketFileResponse> {
        return service.downloadMarketsFile(url)
    }
}