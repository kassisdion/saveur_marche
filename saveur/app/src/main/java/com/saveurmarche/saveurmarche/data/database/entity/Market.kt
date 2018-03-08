package com.saveurmarche.saveurmarche.data.database.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Market(@PrimaryKey var id: String = "",
                  var description: String = "",
                  var address: MarketAddress? = null,
                  var webSiteUrl: String? = null,
                  var picture: String? = null,
                  var type: String? = null,
                  var exhibitors: Long? = null,
                  var productDescription: String = "",
                  var name: String = "") : RealmObject()