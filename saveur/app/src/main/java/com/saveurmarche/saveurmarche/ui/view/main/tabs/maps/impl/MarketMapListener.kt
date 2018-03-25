package com.saveurmarche.saveurmarche.ui.view.main.tabs.maps.impl

import com.saveurmarche.saveurmarche.data.database.entity.Market

interface MarketMapListener {
    fun onMarketClicked(market: Market)
    fun onMarketsClicked(items: MutableCollection<Market>)
}
