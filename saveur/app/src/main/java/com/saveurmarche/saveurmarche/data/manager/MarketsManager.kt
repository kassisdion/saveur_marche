package com.saveurmarche.saveurmarche.data.manager

import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.network.SaveurRequestManager
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import com.saveurmarche.saveurmarche.helper.realm.RxRealmHelper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

class MarketsManager(private val requestManager: SaveurRequestManager,
                     private val preferenceManager: SaveurPreferenceManager) {

    /*
    ************************************************************************************************
    ** Public fun
    ************************************************************************************************
     */
    fun fetchMarkets(): Completable {
        //TODO use API when ready
        //requestManager.getMarketUrl(preferenceManager.lastJsonFetchData)

        return Single
                .just(with(mutableListOf<Market>()) {
                    for (i in 0..2) {
                        add(Market(0.0, 0.0, "Charcuterie $i", "Viande"))
                    }
                    for (i in 2..4) {
                        add(Market(0.0, 0.0, "Charcuterie $i", "BIO"))
                    }
                    for (i in 0..2) {
                        add(Market(0.0, 0.0, "Legume $i", "Bio"))
                    }
                    for (i in 2..4) {
                        add(Market(0.0, 0.0, "Legume $i", "Vert"))
                    }
                    for (i in 0..2) {
                        add(Market(0.0, 0.0, "Fromage $i", "Fromage"))
                    }
                    for (i in 2..4) {
                        add(Market(0.0, 0.0, "Fromage $i", "Bio"))
                    }

                    this
                })
                .flatMapCompletable { data ->
                    RxRealmHelper.doTransactional(Consumer {
                        it.delete(Market::class.java)
                        it.insert(data)
                    })
                }
    }

    fun getLocalMarkets(): Maybe<List<Market>> {
        return RxRealmHelper.getList(Function { realm ->
            realm.where(Market::class.java).findAll()
        })
    }
}
