package com.saveurmarche.saveurmarche.data.resource

import android.app.Application
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val app: Application) {

    fun getString(@StringRes resId: Int): String {
        return app.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return app.getString(resId, *formatArgs)
    }

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(app, drawableRes)
    }

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(app, colorRes)
    }
}