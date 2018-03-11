package com.saveurmarche.saveurmarche.ui.view.base

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Trace
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected val TAG: String = javaClass.simpleName

    /*
    ************************************************************************************************
    ** Fun every child will have to implement
    ************************************************************************************************
    */
    /**
     * get the layout id
     * it will be use under onCreate()
     * as setContentView(getLayoutResource());
     */
    @LayoutRes
    protected abstract fun getLayoutResource(): Int


    /**
     * init the activity here
     * This method is equivalent to onCreate()
     */
    protected abstract fun init(savedInstanceState: Bundle?)

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
    */
    /**
     * Will:
     * -initialise the view with [getLayoutResource]
     * -call [init]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onPreInflate(savedInstanceState)

        setContentView(getLayoutResource())

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            initWithTrace(savedInstanceState)
        } else {
            initWithoutTrace(savedInstanceState)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /*
    ************************************************************************************************
    ** Fun child could override
    ************************************************************************************************
    */
    protected open fun onPreInflate(savedInstanceState: Bundle?) {

    }

    /*
    ************************************************************************************************
    ** Private method
    ************************************************************************************************
    */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun initWithTrace(savedInstanceState: Bundle?) {
        try {
            Trace.beginSection(TAG + " init")
            init(savedInstanceState)
        } finally {
            Trace.endSection()
        }
    }

    private fun initWithoutTrace(savedInstanceState: Bundle?) {
        init(savedInstanceState)
    }
}