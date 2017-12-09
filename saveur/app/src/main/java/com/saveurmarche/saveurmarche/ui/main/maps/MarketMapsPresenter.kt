package com.saveurmarche.saveurmarche.ui.main.maps

/**
 * Listens to user actions from the UI ([MarketsMapFragment]), retrieves the data and updates
 * the UI as required.
 */
class MarketMapsPresenter(
        private val marketsMapView: MarketsMapContract.View
) : MarketsMapContract.Presenter {

    init {
        marketsMapView.presenter = this
    }

    override fun start() {
        retrieveMarketAndPopulate()
    }

    private fun retrieveMarketAndPopulate() {

    }
}