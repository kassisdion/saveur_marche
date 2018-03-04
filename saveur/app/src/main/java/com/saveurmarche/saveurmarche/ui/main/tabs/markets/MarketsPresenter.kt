package com.saveurmarche.saveurmarche.ui.main.tabs.markets

import android.text.Editable
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.ui.base.BasePresenter
import javax.inject.Inject

class MarketsPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<MarketsContract.View>(),
        MarketsContract.Presenter {

    /*
    ************************************************************************************************
    **  Private field
    ************************************************************************************************
    */
    private val mData: MutableList<Market> = mutableListOf()

    /*
    ************************************************************************************************
    **  MarketsContract.Presenter implementation
    ************************************************************************************************
    */
    override fun setupView() {
        view?.showLoading(true)
        fetchData()
    }

    override fun onFilterCtaClicked() {
    }

    override fun onItemClicked(position: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onAfterTextChanged(s: Editable?) {
        val newData = mData.filter { market ->
            val input = s?.toString()?.toLowerCase() ?: ""

            val nameMatch = market.name.toLowerCase().contains(input)
            val typeMatch = market.type?.toLowerCase()?.contains(input) ?: false

            nameMatch || typeMatch
        }

        view?.setData(newData)
    }

    override fun onSwipeRefreshLayoutActivated() {
        fetchData()
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun fetchData() {
        registerDisposable(marketManager.getLocalMarkets()
                .doAfterTerminate({
                    view?.showLoading(false)
                })
                .subscribe(
                        {
                            mData.clear()
                            mData.addAll(it)
                            view?.setData(it)
                        },
                        {
                            //Should display error
                        }
                ))
    }
}