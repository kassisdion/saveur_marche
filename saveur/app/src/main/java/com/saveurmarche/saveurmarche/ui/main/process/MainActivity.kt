package com.saveurmarche.saveurmarche.ui.main.process

import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.transition.Explode
import android.transition.Transition
import android.view.MenuItem
import android.view.Window
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.ui.base.BaseActivity
import com.saveurmarche.saveurmarche.ui.main.tabs.maps.MarketsMapFragment
import com.saveurmarche.saveurmarche.ui.main.tabs.markets.MarketsFragment

class MainActivity : BaseActivity() {
    companion object {
        private const val SELECTED_ITEM = "arg_selected_item"
    }

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
     */
    private val mItems = hashMapOf(
            R.id.action_maps to object : BarItem() {
                override fun getFragment(): Fragment {
                    return MarketsMapFragment.newInstance()
                }
            },
            R.id.action_market to object : BarItem() {
                override fun getFragment(): Fragment {
                    return MarketsFragment.newInstance()
                }

            }
    )

    private var mSelectedItem: Int = 0
    private lateinit var mBottomNavigationView: BottomNavigationView

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun onPreInflate(savedInstanceState: Bundle?) {
        setupTransition()
    }

    override fun init(savedInstanceState: Bundle?) {
        mBottomNavigationView = findViewById(R.id.MainActivityBottomNavigationView)
        mBottomNavigationView.animation = null

        setupBottomNav()

        if (savedInstanceState != null) {
            val oldIndex = savedInstanceState.getInt(SELECTED_ITEM, 0)
            selectFragment(mBottomNavigationView.menu.findItem(oldIndex))
        } else {
            selectFragment(mBottomNavigationView.menu.getItem(0))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, mSelectedItem)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val firstItem = mBottomNavigationView.menu.getItem(0)
        if (mSelectedItem == firstItem.itemId) {
            super.onBackPressed()
        } else {
            selectFragment(firstItem)
        }
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun setupTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

                //set the transition
                val transition: Transition = Explode()
                transition.duration = 5000
                enterTransition = transition
                exitTransition = transition
            }
        }
    }

    private fun setupBottomNav() {
        mBottomNavigationView.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            true
        }
    }

    private fun selectFragment(item: MenuItem) {
        if (mSelectedItem != item.itemId) {
            // update selected item
            mSelectedItem = item.itemId

            // uncheck the other items.
            for (i in 0 until mBottomNavigationView.menu.size() - 1) {
                val menuItem = mBottomNavigationView.menu.getItem(i)
                menuItem.isChecked = (menuItem.itemId == item.itemId)
            }

            updateToolbarText(item.title)

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.MainActivityContainer, mItems[item.itemId]!!.fragment)
                    .commit()
        }
    }

    private fun updateToolbarText(text: CharSequence) {
        supportActionBar?.let {
            it.title = text
        }
    }
}