package com.saveurmarche.saveurmarche.extension

import android.graphics.Point
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.saveurmarche.saveurmarche.helper.logD

fun GoogleMap.displayMarker(markerName: String,
                            markerColor: Float,
                            latitude: Double,
                            longitude: Double) {
    displayMarker(markerName, BitmapDescriptorFactory.defaultMarker(markerColor), latitude, longitude)
}

fun GoogleMap.displayMarker(markerName: String,
                            markerIcon: BitmapDescriptor,
                            latitude: Double,
                            longitude: Double): Marker {
    logD("MapHelper", { "displaying marker:" + "markerName=" + markerName + "latitude=" + latitude + "longitude=" + longitude})

    // create marker
    val marker = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(markerName)
            .icon(markerIcon)

    // adding marker
    return addMarker(marker)
}

fun GoogleMap.displayArea(corners: List<Point>,
                          color: Int) {
    // Instantiates a new Polyline object and adds points to define a rectangle
    val rectOptions = PolygonOptions()
    for (corner in corners) {
        rectOptions.add(LatLng(corner.x.toDouble(), corner.y.toDouble()))
    }
    rectOptions.strokeColor(color)

    //add the polygon to the map
    addPolygon(rectOptions)
}
