package com.saveurmarche.saveurmarche.helper.permission

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.widget.Button
import com.karumi.dexter.MultiplePermissionsReport

import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.saveurmarche.saveurmarche.R

object GeoPermissionHelper {
    private val NEEDED_PERMISSIONS = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    /**
     * Simple interface used to register to Permission result
     */
    private interface RationalDialogListener {

        fun onPositiveClickListener()

        fun onNegativeClickListener()

        fun onDialogDismissed()
    }

    /**
     * Will check [NEEDED_PERMISSIONS] status:
     * -If never asked: see [.showJustificationDialog]
     * -If already asked: see [PermissionHelper.askForPermissionWithDexter]
     *
     * @return dialog to display
     */
    fun ask(activity: Activity,
            permissionListener: SimplePermissionsListener): Dialog? {
        val status = PermissionHelper.getPermissionsStatus(activity, NEEDED_PERMISSIONS)
        when (status) {
            PermissionHelper.GRANTED -> permissionListener.onPermissionGranted(emptyList())
            PermissionHelper.BLOCKED_OR_NEVER_ASKED -> return showJustificationDialog(
                    activity,
                    object : RationalDialogListener {
                        override fun onPositiveClickListener() {
                            askWithDexter(activity, permissionListener)
                        }

                        override fun onNegativeClickListener() {}

                        override fun onDialogDismissed() {}
                    })
            else -> askWithDexter(activity, permissionListener)
        }

        return null
    }

    fun shouldAsk(context: Context): Boolean {
        return !hasPermission(context)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showJustificationDialog(context: Context,
                                        dialogListener: RationalDialogListener): Dialog? {
        val dialog = Dialog(context, R.style.AppTheme_dialog_translucent)
        dialog.setContentView(R.layout.dialog_geoloc_permission)

        val positiveButton = dialog.findViewById<Button>(R.id.button_give_geoloc)
        positiveButton.setOnClickListener { _ ->
            dialog.dismiss()
            dialogListener.onPositiveClickListener()
        }

        val negativeButton = dialog.findViewById<Button>(R.id.button_dismiss)
        negativeButton.setOnClickListener { _ ->
            dialog.dismiss()
            dialogListener.onNegativeClickListener()
        }

        dialog.setOnDismissListener { _ -> dialogListener.onDialogDismissed() }
        dialog.show()

        return dialog
    }
    private fun askWithDexter(activity: Activity,
                              permissionListener: SimplePermissionsListener) {
        PermissionHelper.askForPermissionsWithDexter(
                activity,
                object : MultiplePermissionsListener {

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report != null) {
                            if (report.areAllPermissionsGranted()) {
                                permissionListener.onPermissionGranted(report.grantedPermissionResponses)
                            } else {
                                permissionListener.onPermissionDenied(report.deniedPermissionResponses)
                            }
                        } else {
                            permissionListener.onPermissionDenied(emptyList())
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        showJustificationDialog(
                                activity,
                                object : RationalDialogListener {
                                    override fun onPositiveClickListener() {
                                        token?.continuePermissionRequest()
                                    }

                                    override fun onNegativeClickListener() {
                                        token?.cancelPermissionRequest()
                                    }

                                    override fun onDialogDismissed() {
                                        token?.cancelPermissionRequest()
                                    }
                                })
                    }

                },
                NEEDED_PERMISSIONS)
    }

    private fun hasPermission(context: Context): Boolean {
        return NEEDED_PERMISSIONS
                .map { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
                .any { it }
    }
}