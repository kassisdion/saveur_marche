package com.saveurmarche.saveurmarche.helper

import android.util.Log
import com.saveurmarche.saveurmarche.BuildConfig

/**
 * See http://kotlin4android.com/debug-logging-in-kotlin/
 */
inline fun logV(tag: String,
                msg: () -> String) {
    if (BuildConfig.LOG) {
        Log.v(tag, msg())
    }
}

inline fun logD(tag: String,
                msg: () -> String) {
    if (BuildConfig.LOG) {
        Log.d(tag, msg())
    }
}

inline fun logI(tag: String,
                msg: () -> String) {
    if (BuildConfig.LOG) {
        Log.i(tag, msg())
    }
}

inline fun logW(tag: String,
                msg: () -> String) {
    if (BuildConfig.LOG) {
        Log.w(tag, msg())
    }
}

inline fun logE(tag: String,
                msg: () -> String) {
    if (BuildConfig.LOG) {
        Log.e(tag, msg())
    }
}

inline fun logE(tag: String,
                msg: () -> String,
                error: Throwable?) {
    if (BuildConfig.LOG) {
        Log.e(tag, msg(), error)
    }
}