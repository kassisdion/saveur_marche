package com.saveurmarche.saveurmarche.ui.view.main.tabs.maps

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
import com.saveurmarche.saveurmarche.ui.view.base.BaseFragment
import javax.inject.Inject
import android.location.LocationManager
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher

class MarketsMapFragment : BaseFragment(), MarketsMapContract.View {

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private lateinit var mMap: MarketMap
    private lateinit var mProgressView: View
    private lateinit var mSearchBlock: View
    private lateinit var mMyLocationButton: FloatingActionButton
    private lateinit var mAppCompatEditText: AppCompatEditText
    private lateinit var mFilterCta: View

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

        retrieveWidget(rootView)
        setupView(savedInstanceState)

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
            mMap.draw(data)
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
                context?.let {
                    //Init the mapView
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
                    googleMap.uiSettings.isMyLocationButtonEnabled = false

                    mMap = MarketMap(it, googleMap)

                    mPresenter.onGoogleMapRetrieved()
                }
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
                mMyLocationButton.setOnClickListener({ onMyLocationClicked() })
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

    private fun setupView(savedInstanceState: Bundle?) {
        //Setup filter CTA
        mFilterCta.setOnClickListener({
            mPresenter.onFilterCtaClicked()
        })

        //Setup editText
        mAppCompatEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPresenter.onTextChanged(s, start, before, count)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mPresenter.onBeforeTextChanged(s, start, count, after)
            }

            override fun afterTextChanged(s: Editable?) {
                mPresenter.onAfterTextChanged(s)
            }
        })

        //init MapView
        mMapView?.let {
            it.onCreate(savedInstanceState)
            it.onResume()//We display the map immediately
        }
    }

    @SuppressLint("MissingPermission")
    private fun onMyLocationClicked() {
        context?.let {
            //Acquire a reference to the system Location Manager
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager


            //Acquire the user's location
            val selfLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            //Move the map to the user's location
            mMap.moveCamera(selfLocation.latitude, selfLocation.longitude, zoom = 15f)
        }
    }

    private fun retrieveWidget(rootView: View) {
        with(rootView) {
            mFilterCta = findViewById(R.id.SearchImageViewFilter)
            mMapView = findViewById(R.id.mapView)
            mProgressView = findViewById(R.id.progress)
            mSearchBlock = findViewById(R.id.search_block)
            mMyLocationButton = findViewById(R.id.myLocationButton)
            mAppCompatEditText = rootView.findViewById(R.id.SearchAppCompatEditText)
        }
    }
}