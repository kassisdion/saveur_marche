package com.saveurmarche.saveurmarche.ui.main.maps

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.RequiresPermission
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
    ** MarketsMapContract.View implementation
    ************************************************************************************************
    */
    override fun showLoading(start: Boolean) {
        with(mProgressView) {
            visibility = if (start) View.VISIBLE else View.GONE
        }
    }

    override fun moveCamera(latitude: Double, longitude: Double, zoom: Float) {
        with(mMap) {
            moveCamera(latitude, longitude, zoom)
        }
    }

    override fun drawMarketOnMap(data: ArrayList<Market>) {
        context?.let {
            mMap.draw(it, data)
        }
    }

    override fun navigateToMarketDetail(market: Market) {
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

    override fun retrieveMap() {
        mMapView?.let {
            //retrieve GoogleMap from mapView
            it.getMapAsync({ googleMap ->
                //Init the mapView
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
                googleMap.setOnMarkerClickListener(this)

                mMap = MarketMap(googleMap)

                presenter.onMapRetrieved()
            })
        }
    }

    override fun checkPermission() {
        //Should check right
        context?.let {
            if (GeoPermissionHelper.shouldAsk(it)) {
                activity?.let {
                    GeoPermissionHelper.ask(it,
                            object : SimplePermissionsListener {
                                override fun onPermissionGranted(response: List<PermissionGrantedResponse>) {
                                    logD(TAG, { "checkPermissionAndInitMap > permission granted" })
                                    presenter.onPermissionGranted(response)
                                }

                                override fun onPermissionDenied(response: List<PermissionDeniedResponse>) {
                                    logE(TAG, { "checkPermissionAndInitMap > permission denied" })
                                    presenter.onPermissionDenied(response)
                                }
                            })
                }
            } else {
                presenter.onPermissionAlreadyGranted()
            }
        }
    }

    @RequiresPermission(anyOf = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    override fun initMap(minTime: Long, minDistance: Float) {
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

                locationManager.requestLocationUpdates(provider, minTime, minDistance, this)
            }

            //Center the googleMap map on the userLocation
            with(mMap) {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap.isMyLocationEnabled = true
            }
        }
    }

    /*
    ************************************************************************************************
    ** OnMarkerClickListener implementation
    ************************************************************************************************
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        logD(TAG, { "onMarkerClick" })
        return presenter.onMarkerClick(marker)
    }

    /*
    ************************************************************************************************
    ** LocationListener implementation
    ************************************************************************************************
     */
    override fun onLocationChanged(location: Location?) {
        logD(TAG, { "onLocationChanged" })
        presenter.onLocationChanged(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        logD(TAG, { "onStatusChanged" })
        presenter.onStatusChanged(provider, status, extras)
    }

    override fun onProviderEnabled(provider: String?) {
        logD(TAG, { "onProviderEnabled" })
        presenter.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String?) {
        logD(TAG, { "onProviderDisabled" })
        presenter.onProviderDisabled(provider)
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
            } catch (e: android.content.res.Resources.NotFoundException) {
                //DO want you want, fk instant run
            }
        }
    }
}