package com.saveurmarche.saveurmarche.ui.view.main.tabs.markets

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.database.entity.Market

class MarketAdapter : BaseQuickAdapter<Market, BaseViewHolder>(R.layout.item_market) {

    /*
    ************************************************************************************************
    ** BaseQuickAdapter implementation
    ************************************************************************************************
    */
    override fun convert(helper: BaseViewHolder, item: Market) {
        helper.setText(R.id.itemMarketTextViewName, "${item.name} (${item.address!!.city})")
        helper.setText(R.id.itemMarketTextViewHour, "9H00 - 12H00")
    }
}
