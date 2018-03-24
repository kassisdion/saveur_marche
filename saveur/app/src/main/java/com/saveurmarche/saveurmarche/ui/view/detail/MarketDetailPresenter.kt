package com.saveurmarche.saveurmarche.ui.view.detail

import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.ui.view.base.BasePresenter
import javax.inject.Inject

class MarketDetailPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<MarketDetailContract.View>(),
        MarketDetailContract.Presenter {
    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private lateinit var mMarket: Market

    /*
    ************************************************************************************************
    ** MarketDetailContract.Presenter implementation
    ************************************************************************************************
    */
    override fun setArgs(marketId: String) {
        marketManager.getLocalMarketById(marketId).let {
            if (it == null) {
                view?.stopProcess()
            } else {
                mMarket = it
            }
        }
        populateView()
    }

    private fun populateView() {
        view?.seName(mMarket.displayableName)
        view?.setHour("9H00 - 12H00")
        view?.setDescription(mMarket.displayableDescription)
        view?.setProductDescription(mMarket.displayableProductDescription)
        view?.setPicture(mMarket.picture)
        view?.setDistance("Distance inconnue")
        view?.setMarketUrl(mMarket.webSiteUrl ?: "")
    }
}