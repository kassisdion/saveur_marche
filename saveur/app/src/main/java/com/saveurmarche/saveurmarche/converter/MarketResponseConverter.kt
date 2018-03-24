package com.saveurmarche.saveurmarche.converter

import com.saveurmarche.saveurmarche.api.response.MarketFileResponse
import com.saveurmarche.saveurmarche.data.database.entity.Market

import io.reactivex.functions.Function

class MarketResponseConverter : Function<MarketFileResponse.MarketResponse, Market> {
    @Throws(Exception::class)
    override fun apply(response: MarketFileResponse.MarketResponse): Market {
        return Market(response.id,
                response.description,
                AddressResponseConverter().apply(response.address),
                response.webSiteUrl,
                response.picture,
                response.type,
                response.exhibitors,
                response.productDescription,
                response.name
        )
    }
}
