package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.text.Editable
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BaseView

interface MarketsMapContract {

    interface View : BaseView {
        fun showLoading(start: Boolean)
        fun moveCamera(latitude: Double, longitude: Double, zoom: Float)
        fun drawMarketOnMap(data: ArrayList<Market>)
        fun navigateToMarketDetail(market: Market)
        fun retrieveGoogleMap()
        fun checkPermission()
        fun setupMapView(minTime: Long, minDistance: Float)
    }

    interface Presenter {
        fun onMarkerClick(marker: Marker?): Boolean
        fun onGoogleMapRetrieved()
        fun onGeoPermissionGranted(response: List<PermissionGrantedResponse>?)
        fun onGeoPermissionDenied(response: List<PermissionDeniedResponse>)
        fun setupView()
        fun onFilterCtaClicked()
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onAfterTextChanged(s: Editable?)
    }
}