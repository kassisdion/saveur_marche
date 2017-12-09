package com.saveurmarche.saveurmarche.ui.main

import android.os.Bundle
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.extension.replaceFragmentInActivity
import com.saveurmarche.saveurmarche.extension.setupActionBar
import com.saveurmarche.saveurmarche.ui.base.BaseActivity
import com.saveurmarche.saveurmarche.ui.main.maps.MarketMapsPresenter
import com.saveurmarche.saveurmarche.ui.main.maps.MarketsMapFragment

class MainActivity : BaseActivity() {

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        // Set up the toolbar.
        setupActionBar(R.id.toolbar) {
            title = getString(R.string.app_name)
        }

        val marketMapFragment = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as MarketsMapFragment? ?:
                MarketsMapFragment.newInstance().also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }

        // Create the presenter
        MarketMapsPresenter(marketMapFragment)
    }
}