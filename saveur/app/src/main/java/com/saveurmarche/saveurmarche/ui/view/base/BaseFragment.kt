package com.saveurmarche.saveurmarche.ui.view.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

abstract class BaseFragment : Fragment(), Serializable {
    protected val TAG = javaClass.simpleName

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    protected var mContext: Context? = null

    /*
    ************************************************************************************************
    ** Method every child will have to implement
    ************************************************************************************************
    */
    /**
     * get the layout id
     * it will be use under onCreateView()
     * as inflater.inflate(getLayoutResource(), ...)
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    /*
    ************************************************************************************************
    ** Public method
    ************************************************************************************************
    */
    fun isUiBinded(): Boolean {
        return mContext != null
    }

    /**
     * init the fragment, this is the equivalent of onCreateView
     */
    protected abstract fun init(rootView: View,
                                savedInstanceState: Bundle?)

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
    */
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = layoutInflater.inflate(layoutResource, container, false)
        mContext = context
        return rootView
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view, savedInstanceState)
    }

    override fun onDestroyView() {
        mContext = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}