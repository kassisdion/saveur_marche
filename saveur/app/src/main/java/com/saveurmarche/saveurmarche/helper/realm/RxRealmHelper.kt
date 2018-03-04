package com.saveurmarche.saveurmarche.helper.realm

import android.os.Build
import android.os.HandlerThread
import android.os.Process
import com.saveurmarche.saveurmarche.helper.logE
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults
import java.util.concurrent.atomic.AtomicReference

object RxRealmHelper {

    private val TAG = RxRealmHelper::class.java.simpleName

    /*
    ************************************************************************************************
    ** Public fun
    ************************************************************************************************
    */
    fun <T : RealmObject> listenList(query: Function<Realm, RealmResults<T>>): Flowable<List<T>> {
        val realmReference = AtomicReference<Realm>(null)
        return listenRealmResults(query, realmReference)
                .map { result -> realmReference.get().copyFromRealm(result) }
                .subscribeOn(Schedulers.io())
    }

    fun <T : RealmObject> listenElement(query: Function<Realm, RealmResults<T>>): Flowable<T> {
        val realmReference = AtomicReference<Realm>(null)
        return listenRealmResults(query, realmReference)
                .filter { result -> !result.isEmpty() }
                .map { result -> realmReference.get().copyFromRealm(result.first()!!) }
                .subscribeOn(Schedulers.io())
    }

    fun <T : RealmObject> getList(query: Function<Realm, RealmResults<T>>): Maybe<List<T>> {
        return Maybe.create { emitter ->
            val realm = RealmHelper.getDefaultInstance()
            val result = query.apply(realm)
            if (result != null && result.isLoaded && result.isValid) {
                emitter.onSuccess(realm.copyFromRealm(result))
            } else {
                emitter.onComplete()
            }
            emitter.setCancellable({ realm.close() })
        }
    }

    fun <T : RealmObject> getElement(query: Function<Realm, T>): Maybe<T> {
        return Maybe.create { emitter ->
            val realm = Realm.getDefaultInstance()
            val result = query.apply(realm)
            if (result != null && result.isLoaded && result.isValid) {
                emitter.onSuccess(realm.copyFromRealm(result))
            } else {
                emitter.onComplete()
            }
            emitter.setCancellable({ realm.close() })
        }
    }

    fun doTransactional(transaction: Consumer<Realm>): Completable {
        return Completable
                .fromAction {
                    var realm: Realm? = null
                    try {
                        realm = RealmHelper.getDefaultInstance()
                        realm.executeTransaction { instance ->
                            try {
                                transaction.accept(instance)
                            } catch (e: Exception) {
                                logE(TAG, { "doTransactional > fail" }, e)
                            }
                        }
                    } finally {
                        if (realm != null) {
                            realm.close()
                        }
                    }
                }
                .subscribeOn(Schedulers.computation())
    }

    /*
    ************************************************************************************************
    ** Private func
    ************************************************************************************************
    */
    private fun <T : RealmObject> listenRealmResults(query: Function<Realm, RealmResults<T>>,
                                                     realmReference: AtomicReference<Realm>): Flowable<RealmResults<T>> {
        val dbHandler = createDbHandler()
        val scheduler = AndroidSchedulers.from(dbHandler.looper)
        return Flowable.create<RealmResults<T>>(
                { emitter ->
                    val realm = Realm.getDefaultInstance()
                    realmReference.set(realm)

                    val listener = RealmChangeListener<RealmResults<T>> { result ->
                        val isInvalid = emitter.isCancelled || !result.isLoaded || !result.isValid
                        if (!isInvalid) {
                            emitter.onNext(result)
                        }
                    }

                    val result = query.apply(realm)
                    if (!emitter.isCancelled && result.isLoaded && result.isValid) {
                        emitter.onNext(result)
                    }
                    result.addChangeListener(listener)

                    emitter.setCancellable {
                        result.removeChangeListener(listener)
                        realm.close()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            dbHandler.quitSafely()
                        } else {
                            dbHandler.quit()
                        }
                    }
                },
                BackpressureStrategy.LATEST)
                .subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
    }

    private fun createDbHandler(): HandlerThread {
        val handlerThread = HandlerThread("RealmReadThread", Process.THREAD_PRIORITY_BACKGROUND)
        handlerThread.start()
        return handlerThread
    }
}