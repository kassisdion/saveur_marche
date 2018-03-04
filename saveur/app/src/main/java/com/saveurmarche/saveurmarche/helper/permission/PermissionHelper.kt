package com.saveurmarche.saveurmarche.helper.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.support.annotation.LongDef
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.saveurmarche.saveurmarche.helper.logD

object PermissionHelper {
    private val TAG = PermissionHelper::class.java.simpleName
    /*
    ************************************************************************************************
    ** Public Fields
    ************************************************************************************************
    */
    const val GRANTED = 0L
    const val DENIED = 1L
    const val BLOCKED_OR_NEVER_ASKED = 2L

    @Retention(AnnotationRetention.SOURCE)
    @LongDef(GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED)
    internal annotation class PermissionStatus

    /*
    ************************************************************************************************
    ** Public (static) method
    ************************************************************************************************
    */
    @PermissionStatus
    fun getPermissionsStatus(activity: Activity,
                             androidPermissionsName: List<String>): Long {
        for (permissionName in androidPermissionsName) {
            val status = getPermissionStatus(activity, permissionName)
            if (status != GRANTED) {
                return status
            }
        }

        return GRANTED
    }

    @PermissionStatus
    fun getPermissionStatus(activity: Activity,
                            androidPermissionName: String): Long {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                logD(TAG, { "getPermissionStatus >> BLOCKED_OR_NEVER_ASKED" })
                return BLOCKED_OR_NEVER_ASKED
            }
            logD(TAG, { "getPermissionStatus >> DENIED" })
            return DENIED
        }
        logD(TAG, { "getPermissionStatus >> GRANTED" })
        return GRANTED
    }

    fun askForPermissionWithDexter(activity: Activity,
                                   listener: PermissionListener,
                                   permission: String) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(listener)
                .check()
    }

    fun askForPermissionsWithDexter(activity: Activity,
                                    listener: MultiplePermissionsListener,
                                    permissions: List<String>) {
        Dexter.withActivity(activity)
                .withPermissions(permissions)
                .withListener(listener)
                .check()
    }
}
