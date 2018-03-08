package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.cluster.MarketCluster
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.cluster.MarketClusterRenderer

class MarketMap(val googleMap: GoogleMap) {

    private lateinit var mClusterManager: ClusterManager<MarketCluster>

    /*
    ************************************************************************************************
    ** Public method
    ************************************************************************************************
    */
    fun draw(context: Context,
             markets: List<Market>) {
        //Init cluster
        mClusterManager = ClusterManager(context, googleMap)
        mClusterManager.renderer = MarketClusterRenderer(context, googleMap, mClusterManager)

        googleMap.setOnInfoWindowClickListener(mClusterManager)
        googleMap.setInfoWindowAdapter(mClusterManager.markerManager)
        googleMap.setOnCameraIdleListener(mClusterManager)
        googleMap.setOnMarkerClickListener(mClusterManager)

        //Clear old marker
        googleMap.clear()

        //Populate cluster
        for (market in markets) {
            mClusterManager.addItem(MarketCluster(market))
        }

        //Show cluster
        mClusterManager.cluster()
    }

    fun moveCamera(latitude: Double,
                   longitude: Double) {
        val cameraPosition = CameraPosition.Builder().target(LatLng(latitude, longitude)).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun zoom(zoom: Float) {
        val cameraPosition = CameraPosition.Builder().zoom(zoom).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    /*
    ***********************************************************************************************
    ** Private method
    ***********************************************************************************************
    */
    private fun getMarkerIconFromDrawable(drawable: Drawable,
                                          @ColorInt color: Int): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.setColorFilter(color, PorterDuff.Mode.OVERLAY)
        drawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}