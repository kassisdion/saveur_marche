package com.saveurmarche.saveurmarche.ui.main.tabs.maps

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.helper.permission.GeoPermissionHelper
import com.saveurmarche.saveurmarche.helper.permission.SimplePermissionsListener
import com.saveurmarche.saveurmarche.ui.base.BaseFragment
import javax.inject.Inject

class MarketsMapFragment : BaseFragment(), MarketsMapContract.View {

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
    ** Injection
    ************************************************************************************************
    */
    @Inject
    lateinit var mPresenter: MarketMapsPresenter

    /*
    ************************************************************************************************
    ** Public (static) method
    ************************************************************************************************
    */
    companion object {
        fun newInstance(): MarketsMapFragment {
            return MarketsMapFragment()
        }
    }

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override val layoutResource: Int
        get() = R.layout.fragment_markets_map

    override fun init(rootView: View, savedInstanceState: Bundle?) {
        setupPresenter()
        with(rootView) {
            mMapView = findViewById(com.saveurmarche.saveurmarche.R.id.mapView)
            mProgressView = findViewById(com.saveurmarche.saveurmarche.R.id.progress)
            initMapView(savedInstanceState)
        }
        mPresenter.setupView()
    }

    override fun onResume() {
        super.onResume()

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
                        .setTitle(description)
                        .setMessage(description)
                        .setPositiveButton("Visiter") { _, _ -> }
                        .setNegativeButton("Annuler") { _, _ -> }
                        .show()
            }
        }
    }

    /**
     * Will tell [mMapView] to fetch the map and call [MarketMapsPresenter.onGoogleMapRetrieved]
     * when the map is fetched
     */
    override fun retrieveGoogleMap() {
        mMapView?.let {
            //retrieve GoogleMap from mapView
            it.getMapAsync({ googleMap ->
                //Init the mapView
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))

                mMap = MarketMap(googleMap)

                mPresenter.onGoogleMapRetrieved()
            })
        }
    }

    /**
     * Check the permission and will call:
     * [MarketMapsPresenter.onGeoPermissionGranted] or [MarketMapsPresenter.onGeoPermissionDenied]
     */
    override fun checkPermission() {
        activity?.let {
            if (GeoPermissionHelper.shouldAsk(it)) {
                GeoPermissionHelper.ask(it,
                        object : SimplePermissionsListener {
                            override fun onPermissionGranted(response: List<PermissionGrantedResponse>) {
                                logD(TAG, { "checkPermissionAndInitMap > permission granted" })
                                mPresenter.onGeoPermissionGranted(response)
                            }

                            override fun onPermissionDenied(response: List<PermissionDeniedResponse>) {
                                logE(TAG, { "checkPermissionAndInitMap > permission denied" })
                                mPresenter.onGeoPermissionDenied(response)
                            }
                        })
            } else {
                mPresenter.onGeoPermissionGranted(null)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun setupMapView(minTime: Long, minDistance: Float) {
        context?.let {
            //Center the googleMap map on the userLocation
            with(mMap) {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                googleMap.isMyLocationEnabled = true
            }
        }
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun setupPresenter() {
        SaveurApplication.dataComponent.presenterComponent().inject(this)
        mPresenter.onAttachView(this)
    }

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