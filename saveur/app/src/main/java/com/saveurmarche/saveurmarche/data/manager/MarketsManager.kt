package com.saveurmarche.saveurmarche.data.manager

import com.saveurmarche.saveurmarche.converter.MarketResponseConverter
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.network.SaveurRequestManager
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import com.saveurmarche.saveurmarche.helper.realm.RxRealmHelper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class MarketsManager(private val requestManager: SaveurRequestManager,
                     private val preferenceManager: SaveurPreferenceManager) {

    /*
    ************************************************************************************************
    ** Public fun
    ************************************************************************************************
     */
    fun fetchMarkets(): Completable {
        return requestManager
                .getMarketUrl()
                .flatMapCompletable({ response ->
                    when {
                        response.code() == 204 -> Completable.complete()
                        response.body()?.url != null -> fetchJsonAndUpdateDb(response.body()!!.url!!)
                        else -> Completable.error(IllegalArgumentException("invalid response"))
                    }
                })
                .subscribeOn(Schedulers.io())
    }

    fun getLocalMarket(): Maybe<List<Market>> {
        return RxRealmHelper.getList(Function { realm -> realm.where(Market::class.java).findAll() })
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
     */
    private fun fetchJsonAndUpdateDb(url: String): Completable {
        return requestManager.getMarketsFile(url)
                .flattenAsObservable { markets -> markets.markets }
                .map { marketResponse -> MarketResponseConverter().apply(marketResponse) }
                .toList()
                .doOnSuccess { preferenceManager.lastJsonFetchData = System.currentTimeMillis() / 1000 }
                .flatMapCompletable { data ->
                    RxRealmHelper.doTransactional(Consumer {
                        it.delete(Market::class.java)
                        it.insert(data)
                    })
                }
    }
}
