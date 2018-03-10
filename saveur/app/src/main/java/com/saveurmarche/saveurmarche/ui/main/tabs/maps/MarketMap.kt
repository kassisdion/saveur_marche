package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.cluster.MarketCluster
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.cluster.MarketClusterRenderer

class MarketMap(context: Context, val googleMap: GoogleMap) {

    private var mClusterManager: ClusterManager<MarketCluster> = ClusterManager(context, googleMap)

    init {
        //Init cluster
        mClusterManager.renderer = MarketClusterRenderer(context, googleMap, mClusterManager)

        //Init map
        googleMap.setOnInfoWindowClickListener(mClusterManager)
        googleMap.setInfoWindowAdapter(mClusterManager.markerManager)
        googleMap.setOnCameraIdleListener(mClusterManager)
        googleMap.setOnMarkerClickListener(mClusterManager)

    }

    /*
    ************************************************************************************************
    ** Public method
    ************************************************************************************************
    */
    fun draw(markets: List<Market>) {
        //Clear old marker
        mClusterManager.clearItems()

        //Populate cluster
        for (market in markets) {
            mClusterManager.addItem(MarketCluster(market))
        }

        //Show cluster
        mClusterManager.cluster()
    }

    fun moveCamera(latitude: Double,
                   longitude: Double,
                   zoom: Float? = null,
                   tilt: Float? = null,
                   bearing: Float? = null) {
        moveCamera(LatLng(latitude, longitude), zoom, tilt, bearing)
    }

    fun moveCamera(latLng: LatLng,
                   zoom: Float? = null,
                   tilt: Float? = null,
                   bearing: Float? = null) {
        val cameraPosition = CameraPosition.Builder()
                .apply {
                    target(latLng)
                    if (zoom != null) zoom(zoom)
                    if (tilt != null) tilt(tilt)
                    if (bearing != null) bearing(bearing)
                }
                .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}