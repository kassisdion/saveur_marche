package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Listens to user actions from the UI ([MarketsMapFragment]), retrieves the data and updates
 * the UI as required.
 */
class MarketMapsPresenter @Inject constructor(/*Add needed dependencies here*/) :
        BasePresenter<MarketsMapContract.View>(),
        MarketsMapContract.Presenter {

    companion object {
        private const val MIN_TIME: Long = 10000 //Minimum time between 2 update (in millisecond)
        private const val MIN_DIST: Float = 1.0f //Minimum distance between 2 update (in meter)
        private const val ZOOM: Float = 15.0f
    }

    /*
    ************************************************************************************************
    **  MarketsMapContract.Presenter implementation
    ************************************************************************************************
     */
    override fun setupView() {
        view?.retrieveMap()
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            view?.showLoading(true)

            val latitude = location.latitude
            val longitude = location.longitude

            view?.moveCamera(latitude, longitude, ZOOM)
            view?.drawMarketOnMap(arrayListOf(
                    Market(latitude + 0.005f, longitude, "Marché de gauche", null),
                    Market(latitude, longitude + 0.005f, "Marché de droite" + 0.005f, null),
                    Market(latitude, longitude + 0.005f, "Marché au pif", null)))

            view?.showLoading(false)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null) {
            view?.navigateToMarketDetail(marker.tag as Market)
            return true
        }

        return false
    }

    override fun onMapRetrieved() {
        view?.checkPermission()
    }

    override fun onPermissionGranted(response: List<PermissionGrantedResponse>) {
    }

    override fun onPermissionAlreadyGranted() {
        view?.initMap(MIN_TIME, MIN_DIST)
    }

    override fun onPermissionDenied(response: List<PermissionDeniedResponse>) {
    }
}