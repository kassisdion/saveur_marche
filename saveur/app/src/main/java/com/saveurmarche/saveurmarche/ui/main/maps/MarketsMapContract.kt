package com.saveurmarche.saveurmarche.ui.main.maps

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.data.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import com.saveurmarche.saveurmarche.ui.base.BaseView

interface MarketsMapContract {

    interface View : BaseView<Presenter> {
        fun showLoading(start: Boolean)
        fun moveCamera(latitude: Double, longitude: Double, zoom: Float)
        fun drawMarketOnMap(data: ArrayList<Market>)
        fun navigateToMarketDetail(market: Market)
        fun retrieveMap()
        fun checkPermission()
        fun initMap(minTime: Long, minDistance: Float)
    }

    interface Presenter : BasePresenter {
        fun onLocationChanged(location: Location?)
        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?)
        fun onProviderEnabled(provider: String?)
        fun onProviderDisabled(provider: String?)
        fun onMarkerClick(marker: Marker?): Boolean
        fun onMapRetrieved()
        fun onPermissionGranted(response: List<PermissionGrantedResponse>)
        fun onPermissionAlreadyGranted()
        fun onPermissionDenied(response: List<PermissionDeniedResponse>)
    }
}