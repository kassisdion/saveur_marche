package com.saveurmarche.saveurmarche.data.entity

import android.support.annotation.ColorRes
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.entity.base.BaseEntity

class Market : BaseEntity {
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    var name: String
    var type: String? = null
    val colorType: Int
        @ColorRes
        get() {
            return R.color.black
        }

    constructor(latitude: Double,
                longitude: Double,
                name: String,
                type: String?) {
        this.latitude = latitude
        this.longitude = longitude
        this.name = name
        this.type = type
    }
}