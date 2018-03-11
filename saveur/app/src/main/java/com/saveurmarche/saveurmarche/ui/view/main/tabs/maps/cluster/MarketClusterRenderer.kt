package com.saveurmarche.saveurmarche.ui.view.main.tabs.maps.cluster

import android.content.Context

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MarketClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<MarketCluster>)
    : DefaultClusterRenderer<MarketCluster>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MarketCluster, markerOptions: MarkerOptions) {
        val markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        markerOptions.icon(markerDescriptor).snippet(item.title)
    }

    override fun onBeforeClusterRendered(cluster: Cluster<MarketCluster>, markerOptions: MarkerOptions) {
        //Could customise cluster icon here
        super.onBeforeClusterRendered(cluster, markerOptions)
    }
}
