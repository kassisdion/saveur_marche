package com.saveurmarche.saveurmarche.ui.main.tabs.markets

import android.text.Editable
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.ui.base.BaseView

interface MarketsContract {
    interface View : BaseView {
        fun setData(newData: List<Market>)
        fun showLoading(show: Boolean)
    }

    interface Presenter {
        fun onFilterCtaClicked()
        fun onItemClicked(position: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onAfterTextChanged(s: Editable?)
        fun onSwipeRefreshLayoutActivated()
        fun setupView()
    }
}