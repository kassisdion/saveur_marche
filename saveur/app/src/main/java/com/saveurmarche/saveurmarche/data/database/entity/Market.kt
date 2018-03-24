package com.saveurmarche.saveurmarche.data.database.entity

import com.saveurmarche.saveurmarche.data.database.BaseEntity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Market(@PrimaryKey var id: String = "",
                  var description: String? = null,
                  var address: MarketAddress? = null,
                  var webSiteUrl: String? = null,
                  var picture: String? = null,
                  var type: String? = null,
                  var exhibitors: Long? = null,
                  var productDescription: String? = null,
                  var name: String? = null) : BaseEntity, RealmObject() {
    var displayableDescription = description ?: "Aucune description du marché"
    var displayableProductDescription = productDescription ?: "Aucune description des produits"
    var displayableName = name ?: "Aucun nom"
}