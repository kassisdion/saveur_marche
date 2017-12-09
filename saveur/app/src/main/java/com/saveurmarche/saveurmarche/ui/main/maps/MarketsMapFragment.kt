package com.saveurmarche.saveurmarche.ui.main.maps

import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.entity.Market
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.helper.permission.GeoPermissionHelper
import com.saveurmarche.saveurmarche.helper.permission.SimplePermissionsListener
import com.saveurmarche.saveurmarche.ui.base.BaseFragment

class MarketsMapFragment : BaseFragment(), LocationListener, GoogleMap.OnMarkerClickListener, MarketsMapContract.View {

    companion object {

        val FRAGMENT_NAME: String = MarketsMapFragment::class.java.simpleName

        private val MIN_TIME: Long = 10000 //Minimum time between 2 update (in millisecond)
        private val MIN_DIST: Float = 1.0f //Minimum distance between 2 update (in meter)
        private val ZOOM: Float = 15.0f

        fun newInstance(): MarketsMapFragment {
            return MarketsMapFragment()
        }
    }

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private lateinit var mMap: MarketMap
    private lateinit var mProgressView: View

    private var mMapView: MapView? = null

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override lateinit var presenter: MarketsMapContract.Presenter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_markets_map, container, false)

        with(root) {
            mMapView = findViewById(R.id.mapView)
            mProgressView = findViewById(R.id.progress)
            initMapView(savedInstanceState)
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        presenter.start()
        mMapView?.onResume()
    }

    override fun onPause() {
        mMapView?.onPause()

        super.onPause()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()

        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mMapView?.onLowMemory()
    }

    /*
    ************************************************************************************************
    ** OnMarkerClickListener implementation
    ************************************************************************************************
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null) {
            navigateToMarketDetail(marker.tag as Market)
            return true
        }

        return false
    }

    /*
    ************************************************************************************************
    ** LocationListener implementation
    ************************************************************************************************
     */
    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            context?.let {
                with(mProgressView) {
                    visibility = View.VISIBLE
                }

                val latitude = location.latitude
                val longitude = location.longitude

                logD(TAG, { "set location to: $latitude, $longitude" })

                //update map position
                mMap.moveCamera(latitude, longitude, ZOOM)

                //Should retrieve market around the new position and display them
                mMap.draw(it, arrayListOf(
                        Market(latitude + 0.005f, longitude, "Marché de gauche", null),
                        Market(latitude, longitude + 0.005f, "Marché de droite" + 0.005f, null),
                        Market(latitude, longitude + 0.005f, "Marché au pif", null)))

                with(mProgressView) {
                    visibility = View.GONE
                }
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        logD(TAG, { "status changed: " + provider })
    }

    override fun onProviderEnabled(provider: String?) {
        logD(TAG, { "provider enable: " + provider })
    }

    override fun onProviderDisabled(provider: String?) {
        logD(TAG, { "provider disable:" + provider })
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
     */
    private fun initMapView(savedInstanceState: Bundle?) {
        mMapView?.let {
            try {
                it.onCreate(savedInstanceState)
                it.onResume()//We display the map immediately

                //retrieve GoogleMap from mapView
                it.getMapAsync({ googleMap ->
                    //Init the mapView
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
                    googleMap.setOnMarkerClickListener(this)

                    mMap = MarketMap(googleMap)
                    checkPermissionAndInitMap()
                })
            } catch (e: android.content.res.Resources.NotFoundException) {
                //DO want you want, fk instant run
            }
        }
    }

    private fun checkPermissionAndInitMap() {
        //Should check right
        context?.let {
            if (GeoPermissionHelper.shouldAsk(it)) {
                activity?.let {
                    GeoPermissionHelper.ask(it,
                            object : SimplePermissionsListener {
                                override fun onPermissionGranted(response: List<PermissionGrantedResponse>) {
                                    logD(TAG, { "checkPermissionAndInitMap > permission granted" })
                                    initMap()
                                }

                                override fun onPermissionDenied(response: List<PermissionDeniedResponse>) {
                                    logE(TAG, { "checkPermissionAndInitMap > permission denied" })
                                }
                            })
                }
            } else {
                initMap()
            }
        }
    }

    private fun initMap() {
        context?.let {
            // Acquire a reference to the system Location Manager
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Register the listener with the Location Manager to receive location updates
            for (provider in locationManager.getProviders(false)) {

                if (locationManager.isProviderEnabled(provider)) {
                    //onProviderEnabled() will not be called if the provider is already enable so we call it
                    onProviderEnabled(LocationManager.GPS_PROVIDER)

                    //get location fix
                    val lastLocation = locationManager.getLastKnownLocation(provider)
                    if (lastLocation != null) {
                        //Update the MarketMap
                        onLocationChanged(lastLocation)
                    }
                }

                locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, this)
            }

            //Center the googleMap map on the userLocation
            with(mMap) {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap.setMyLocationEnabled(true)
            }
        }
    }

    private fun navigateToMarketDetail(market: Market) {
        context?.let {
            with(market) {
                AlertDialog.Builder(it)
                        .setTitle(name)
                        .setMessage("Bienvenue sur la page de detail du marché $name")
                        .setPositiveButton("Visiter") { _, _ -> }
                        .setNegativeButton("Annuler") { _, _ -> }
                        .show()
            }
        }
    }
}