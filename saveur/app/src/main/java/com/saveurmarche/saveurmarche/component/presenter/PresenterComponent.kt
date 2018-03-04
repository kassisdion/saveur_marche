package com.saveurmarche.saveurmarche.component.presenter

import com.saveurmarche.saveurmarche.ui.launch.LaunchActivity
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.MarketsMapFragment
import com.saveurmarche.saveurmarche.ui.main.tabs.markets.MarketsFragment
import dagger.Subcomponent

@PresenterScope
@Subcomponent
interface PresenterComponent {
    fun inject(launchActivity: LaunchActivity)
    fun inject(launchActivity: MarketsMapFragment)
    fun inject(marketsFragment: MarketsFragment)
}