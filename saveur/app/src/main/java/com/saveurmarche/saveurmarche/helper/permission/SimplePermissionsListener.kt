package com.saveurmarche.saveurmarche.helper.permission

import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse

interface SimplePermissionsListener {
    fun onPermissionGranted(response: List<PermissionGrantedResponse>)

    fun onPermissionDenied(response: List<PermissionDeniedResponse>)
}