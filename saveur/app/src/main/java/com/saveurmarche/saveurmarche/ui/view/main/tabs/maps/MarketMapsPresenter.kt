package com.saveurmarche.saveurmarche.ui.view.main.tabs.maps

import android.text.Editable
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.data.matcher.impl.MarketMatcher
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.ui.view.base.BasePresenter
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
    ** Private field
    ************************************************************************************************
     */
    private var mData: List<Market>? = null
    private var mHasPermission = false
    private var mLastDisplayedMarketDetail: Market? = null

    /*
    ************************************************************************************************
    ** MarketsMapContract.Presenter implementation
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
        view?.setupMapView(MIN_TIME, MIN_DIST, mHasPermission)
        fetchData()
    }

    override fun onGeoPermissionGranted(response: List<PermissionGrantedResponse>?) {
        mHasPermission = true
        view?.retrieveGoogleMap()
    }

    override fun onGeoPermissionDenied(response: List<PermissionDeniedResponse>) {
        mHasPermission = false
        view?.retrieveGoogleMap()
    }

    override fun onFilterCtaClicked() {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onAfterTextChanged(s: Editable?) {
        mData?.let {
            val newData = it.filter { MarketMatcher.all(s?.toString()?.toLowerCase() ?: "")(it) }
            view?.drawMarketOnMap(ArrayList(newData))
        }
    }

    override fun onMyLocationClicked() {
        view?.centerMapOnUser(ZOOM)
    }

    override fun onMarketsClicked(markets: MutableCollection<Market>) {

    }

    override fun onMarketClicked(market: Market) {
        mLastDisplayedMarketDetail = market

        view?.setMarketDetailVisibility(true)
        view?.setMarketDetailName(market.displayableName)
        view?.setMarketDetailHour("9H00 - 12H00")
        view?.setMarketDetailImage(market.picture)
        view?.setMarketDetailDistance("Distance inconnue")
    }

    override fun onMarketDetailClicked() {
        mLastDisplayedMarketDetail?.let {
            view?.navigateToMarketDetail(it)
        }
    }

    override fun onMarketDetailCloseClicked() {
        mLastDisplayedMarketDetail = null
        view?.setMarketDetailVisibility(false)
    }

    /*
    ************************************************************************************************
    ** Private method
    ************************************************************************************************
     */
    private fun fetchData() {
        registerDisposable(marketsManager.getLocalMarket()
                .doOnSubscribe({ view?.showLoading(true) })
                .doAfterTerminate({ view?.showLoading(false) })
                .subscribe(
                        {
                            mData = it
                            view?.drawMarketOnMap(ArrayList(it))
                        },
                        {
                            logE("MarketMapsPresenter", { "setupView > getLocalMarket > fail" }, RxJava2Debug.getEnhancedStackTrace(it))
                        }
                ))
    }
}