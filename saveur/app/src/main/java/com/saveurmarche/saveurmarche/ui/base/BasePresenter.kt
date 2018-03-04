package com.saveurmarche.saveurmarche.ui.base

import android.support.annotation.CallSuper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T : BaseView> {

    private val TAG = javaClass.simpleName

    /*
    ************************************************************************************************
    ** Private field
    ************************************************************************************************
    */
    private val mCompositeDisposable = CompositeDisposable()
    protected var view: T? = null

    /*
    ************************************************************************************************
    ** Protected fun
    ************************************************************************************************
    */
    protected val isViewAttached: Boolean
        get() = view != null

    @CallSuper
    open fun onAttachView(view: T) {
        this.view = view
    }

    @CallSuper
    open fun onDetachView() {
        mCompositeDisposable.clear()
        view = null
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    protected fun registerDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }
}

