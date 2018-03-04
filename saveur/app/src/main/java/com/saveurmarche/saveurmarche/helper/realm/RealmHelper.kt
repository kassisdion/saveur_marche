package com.saveurmarche.saveurmarche.helper.realm

import android.content.Context
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.logE
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException

object RealmHelper {
    private val TAG = RealmHelper::class.java.simpleName

    /*
    ************************************************************************************************
    ** Private val
    ************************************************************************************************
    */
    private val defaultConfiguration: RealmConfiguration by lazy {
        RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
    }

    /*
    ************************************************************************************************
    ** Public fun
    ************************************************************************************************
    */
    fun init(context: Context) {
        logD(TAG) { "init" }

        //Init
        Realm.init(context.applicationContext)

        //Set default configuration
        val realmConfiguration = defaultConfiguration
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    fun getDefaultInstance(): Realm {
        val realm: Realm
        try {
            realm = Realm.getDefaultInstance()
        } catch (e: RealmMigrationNeededException) {
            logE(TAG, { "getDefaultInstance > fail" }, e)
            throw e
        } catch (e: NullPointerException) {
            logE(TAG, { "getDefaultInstance > fail" }, e)
            throw e
        } catch (e: RealmFileException) {
            logE(TAG, { "getDefaultInstance > fail" }, e)
            throw e
        }

        return realm
    }

    fun deleteTable(realm: Realm,
                    classToDelete: Class<out RealmModel>) {
        logD(TAG) { "deleteTable > " + classToDelete.simpleName }

        realm.executeTransaction { instance ->
            //Simply drop the table
            instance.delete(classToDelete)
        }
    }
}