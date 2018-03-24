package com.saveurmarche.saveurmarche.component.presenter

import com.saveurmarche.saveurmarche.ui.view.detail.MarketDetailActivity
import com.saveurmarche.saveurmarche.ui.view.launch.LaunchActivity
import com.saveurmarche.saveurmarche.ui.view.main.tabs.maps.MarketsMapFragment
import com.saveurmarche.saveurmarche.ui.view.main.tabs.markets.MarketsFragment
import dagger.Subcomponent

@PresenterScope
@Subcomponent
interface PresenterComponent {
    fun inject(launchActivity: LaunchActivity)
    fun inject(launchActivity: MarketsMapFragment)
    fun inject(marketsFragment: MarketsFragment)
    fun inject(marketDetailActivity: MarketDetailActivity)
}