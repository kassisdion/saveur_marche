package com.saveurmarche.saveurmarche.data.manager

import com.saveurmarche.saveurmarche.converter.MarketResponseConverter
import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.network.SaveurRequestManager
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.realm.RealmHelper
import com.saveurmarche.saveurmarche.helper.realm.RxRealmHelper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmObject

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
                    when (response.code()) {
                        204 -> {
                            logD("MarketsManager", { "fetchMarkets > 204" })
                            Completable.complete()
                        }
                        200 -> {
                            logD("MarketsManager", { "fetchMarkets > 200" })
                            fetchJsonAndUpdateDb(response.body()!!.url!!)
                        }
                        else -> {
                            logD("MarketsManager", { "fetchMarkets > unsupported code" })
                            Completable.error(IllegalArgumentException("invalid response"))
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
    }

    fun getLocalMarket(): Maybe<List<Market>> {
        return RxRealmHelper.getList(Function { realm -> realm.where(Market::class.java).findAll() })
    }

    fun getLocalMarketById(marketId: String): Market? {
        RealmHelper.getDefaultInstance().use {
            val market: Market? = it.where(Market::class.java).contains("id", marketId).findFirst()
            if (market != null) {
                return it.copyFromRealm(market)
            }
        }

        return null
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
     */
    private fun fetchJsonAndUpdateDb(url: String): Completable {
        logD("MarketsManager", { "fetchJsonAndUpdateDb > url=$url" })

        return requestManager.getMarketsFile(url)
                .flattenAsObservable { markets -> markets.markets }
                .map { marketResponse -> MarketResponseConverter().apply(marketResponse) }
                .toList()
                .doOnSuccess {
                    logD("MarketsManager", { "fetchJsonAndUpdateDb > success > got ${it.size} market" })

                    logD("MarketsManager", { "fetchJsonAndUpdateDb > success > saving lastJsonFetchData" })
                    preferenceManager.lastJsonFetchData = System.currentTimeMillis() / 1000
                }
                .flatMapCompletable { data ->
                    logD("MarketsManager", { "fetchJsonAndUpdateDb > success > inserting" })

                    RxRealmHelper.doTransactional(Consumer {
                        it.delete(Market::class.java)
                        it.insert(data)
                    })
                }
    }
}
