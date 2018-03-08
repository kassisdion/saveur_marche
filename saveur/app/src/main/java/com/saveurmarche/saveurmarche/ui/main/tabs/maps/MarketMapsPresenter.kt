package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.location.Location
import android.os.Bundle
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Listens to user actions from the UI ([MarketsMapFragment]), retrieves the data and updates
 * the UI as required.
 */
class MarketMapsPresenter @Inject constructor(private val marketsManager: MarketsManager) :
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
        view?.showLoading(true)
        view?.checkPermission()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.tag?.let {
            view?.navigateToMarketDetail(it as Market)
            return true
        }

        return false
    }

    override fun onGoogleMapRetrieved() {
        view?.setupMapView(MIN_TIME, MIN_DIST)

        registerDisposable(marketsManager.getLocalMarket()
                .doOnSubscribe({ view?.showLoading(true) })
                .doAfterTerminate({ view?.showLoading(false) })
                .subscribe(
                        {
                            view?.drawMarketOnMap(ArrayList(it))
                        },
                        {
                            logE("MarketMapsPresenter", { "setupView > getLocalMarket > fail" }, RxJava2Debug.getEnhancedStackTrace(it))
                        }
                ))
    }

    override fun onGeoPermissionGranted(response: List<PermissionGrantedResponse>?) {
        view?.retrieveGoogleMap()
    }

    override fun onGeoPermissionDenied(response: List<PermissionDeniedResponse>) {
    }
}