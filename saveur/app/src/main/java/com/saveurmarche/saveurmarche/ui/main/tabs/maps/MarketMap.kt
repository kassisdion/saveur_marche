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
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.extension.displayMarker

class MarketMap(val googleMap: GoogleMap) {

    /*
    ************************************************************************************************
    ** Public method
    ************************************************************************************************
    */
    fun draw(context: Context,
             markets: List<Market>) {
        //Clear old marker
        googleMap.clear()

        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.market_drawable)

        //Display new marker
        for (market in markets) {
            val marketColor = ContextCompat.getColor(context, market.colorType)
            val marketIcon = getMarkerIconFromDrawable(iconDrawable!!, marketColor)

            googleMap.displayMarker(
                    market.name,
                    marketIcon,
                    market.latitude,
                    market.longitude).tag = market
        }
    }

    fun moveCamera(latitude: Double,
                   longitude: Double,
                   zoom: Float) {
        val cameraPosition = CameraPosition.Builder().target(LatLng(latitude, longitude)).zoom(zoom).build()
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