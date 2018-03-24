package com.saveurmarche.saveurmarche.converter

import com.saveurmarche.saveurmarche.api.response.MarketFileResponse
import com.saveurmarche.saveurmarche.data.database.entity.MarketAddress

import io.reactivex.functions.Function

class AddressResponseConverter : Function<MarketFileResponse.AdressResponse, MarketAddress> {
    @Throws(Exception::class)
    override fun apply(response: MarketFileResponse.AdressResponse): MarketAddress {
        return MarketAddress(
                response.formatted_address,
                response.zip_code,
                response.city,
                response.country,
                response.longitude,
                response.latitude)
    }
}
