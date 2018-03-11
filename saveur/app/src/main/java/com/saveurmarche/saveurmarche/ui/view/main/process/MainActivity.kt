package com.saveurmarche.saveurmarche.ui.view.main.process

import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.transition.Explode
import android.transition.Transition
import android.view.MenuItem
import android.view.Window
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.ui.view.base.BaseActivity
import com.saveurmarche.saveurmarche.ui.view.main.tabs.maps.MarketsMapFragment
import com.saveurmarche.saveurmarche.ui.view.main.tabs.markets.MarketsFragment

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
                override val fragment: Fragment
                    get() = MarketsMapFragment.newInstance()
            },
            R.id.action_market to object : BarItem() {
                override val fragment: Fragment
                    get() = MarketsFragment.newInstance()
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
        val itemId = item.itemId

        if (mSelectedItem != itemId) {
            // update selected item
            mSelectedItem = itemId

            // uncheck the other items.
            (0 until mBottomNavigationView.menu.size() - 1)
                    .map { mBottomNavigationView.menu.getItem(it) }
                    .forEach { it.isChecked = (it.itemId == itemId) }

            //Update toolbar title
            updateToolbarText(item.title)

            //Show fragment link to the selected item
            showFragment(itemId)
        }
    }

    private fun updateToolbarText(text: CharSequence) {
        supportActionBar?.let {
            it.title = text
        }
    }

    private fun showFragment(itemId: Int) {
        val containerId = R.id.MainActivityContainer
        val tag = "$itemId"
        val transaction = supportFragmentManager.beginTransaction()

        //Hide old one
        supportFragmentManager.findFragmentById(containerId)?.let {
            transaction.hide(it)
        }

        //Show new fragment
        supportFragmentManager.findFragmentByTag(tag).let {
            when (it) {
                null -> transaction.add(containerId, mItems[itemId]!!.fragment, tag)
                else  -> transaction.show(it)
            }
        }

        transaction.commit()
    }
}