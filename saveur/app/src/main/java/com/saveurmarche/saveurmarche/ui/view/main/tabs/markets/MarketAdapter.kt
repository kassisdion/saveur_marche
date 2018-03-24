package com.saveurmarche.saveurmarche.ui.view.main.tabs.markets

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
        helper.setText(R.id.itemMarketTextViewName, item.displayableName)
        helper.setText(R.id.itemMarketTextViewHour, "9H00 - 12H00")

        val image : ImageView = helper.getView(R.id.itemMarketImage)
        Glide.with(image.context)
                .setDefaultRequestOptions(RequestOptions()
                        .placeholder(R.drawable.ic_market_colorhint)
                        .error(R.drawable.ic_market_colorhint))
                .load(item.picture)
                .into(image)

        helper.setText(R.id.itemMarketTextViewDistance, "Distance inconnue")
    }
}
