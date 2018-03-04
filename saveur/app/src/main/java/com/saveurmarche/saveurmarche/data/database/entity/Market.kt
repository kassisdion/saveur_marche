package com.saveurmarche.saveurmarche.data.database.entity

import android.support.annotation.ColorRes
import com.saveurmarche.saveurmarche.R
import io.realm.RealmObject

open class Market(var latitude: Double = 0.0,
                  var longitude: Double = 0.0,
                  var name: String = "",
                  var type: String? = null) : RealmObject() {
    val colorType: Int
        @ColorRes
        get() {
            return R.color.black
        }
}