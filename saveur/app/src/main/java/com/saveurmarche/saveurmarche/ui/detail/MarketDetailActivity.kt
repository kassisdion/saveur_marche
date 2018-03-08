package com.saveurmarche.saveurmarche.ui.detail

import android.os.Bundle
import com.saveurmarche.saveurmarche.R
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BaseActivity

class MarketDetailActivity : BaseActivity() {
    companion object Builder {
        fun newInstance(market: Market) {

        }
    }
    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
     */
    override fun init(savedInstanceState: Bundle?) {
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_market_detail
    }
}