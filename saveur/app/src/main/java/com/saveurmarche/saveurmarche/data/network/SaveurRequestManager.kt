package com.saveurmarche.saveurmarche.data.network

import com.saveurmarche.saveurmarche.api.SaveurServices

class SaveurRequestManager(private val service: SaveurServices.ApiService) {
    fun getMarketUrl(since: Int) = service.getMarketUrl(since)
}