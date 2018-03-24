package com.saveurmarche.saveurmarche.ui.view.detail

import com.saveurmarche.saveurmarche.ui.view.base.BaseView

interface MarketDetailContract {

    interface View: BaseView {
        fun stopProcess()
        fun seName(displayableName: String)
        fun setHour(hour: String)
        fun setPicture(url: String?)
        fun setDescription(displayableDescription: String)
        fun setProductDescription(displayableProductDescription: String)
        fun setDistance(distance: String)
        fun setMarketUrl(url: String)
    }

    interface Presenter {
        fun setArgs(marketId: String)
    }

}