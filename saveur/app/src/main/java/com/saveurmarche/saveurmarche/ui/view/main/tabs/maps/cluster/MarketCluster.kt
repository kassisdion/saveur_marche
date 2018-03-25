package com.saveurmarche.saveurmarche.ui.view.main.tabs.maps.cluster

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.saveurmarche.saveurmarche.data.database.entity.Market

class MarketCluster(val market: Market) : ClusterItem {

    /*
    ************************************************************************************************
    ** ClusterItem implementation
    ************************************************************************************************
    */
    override fun getPosition(): LatLng {
        return LatLng(market.address!!.latitude, market.address!!.longitude)
    }

    override fun getTitle(): String {
        return market.name + market.address!!.city
    }

    override fun getSnippet(): String? {
        return null
    }
}
