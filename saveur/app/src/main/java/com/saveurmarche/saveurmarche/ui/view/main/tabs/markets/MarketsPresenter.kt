package com.saveurmarche.saveurmarche.ui.view.main.tabs.markets

import android.text.Editable
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.data.matcher.impl.MarketMatcher
import com.saveurmarche.saveurmarche.helper.logE
import com.saveurmarche.saveurmarche.ui.view.base.BasePresenter
import javax.inject.Inject

class MarketsPresenter @Inject constructor(private val marketManager: MarketsManager) :
        BasePresenter<MarketsContract.View>(),
        MarketsContract.Presenter {

    /*
    ************************************************************************************************
    **  Private field
    ************************************************************************************************
    */
    private val TAG = MarketsPresenter::class.java.simpleName
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
        view?.redirectToMarketDetail(mData[position])
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onAfterTextChanged(s: Editable?) {
        val newData = mData.filter { MarketMatcher.all(s?.toString()?.toLowerCase() ?: "")(it) }

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
        registerDisposable(marketManager.getLocalMarket()
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
                            logE(TAG, { "fetchData > fail" }, RxJava2Debug.getEnhancedStackTrace(it))
                            //Should display error
                        }
                ))
    }
}