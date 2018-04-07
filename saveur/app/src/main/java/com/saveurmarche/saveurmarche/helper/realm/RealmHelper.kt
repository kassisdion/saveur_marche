package com.saveurmarche.saveurmarche.helper.realm

import android.content.Context
import com.saveurmarche.saveurmarche.helper.logD
import com.saveurmarche.saveurmarche.helper.logE
import io.realm.*
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException

class RealmHelper {

    companion object {
        private val TAG = RealmHelper::class.java.simpleName

        /*
        ************************************************************************************************
        ** Private val
        ************************************************************************************************
        */
        private const val DB_VERSION = 1L

        private val defaultConfiguration: RealmConfiguration by lazy {
            RealmConfiguration.Builder()
                    .schemaVersion(DB_VERSION)
                    .deleteRealmIfMigrationNeeded()
                    .build()
        }

        /*
        ************************************************************************************************
        ** Public fun
        ************************************************************************************************
        */
        fun init(context: Context, onDataLost : () -> Unit) {
            logD(TAG) { "init" }

            Realm.init(context.applicationContext)
            Realm.setDefaultConfiguration(defaultConfiguration)
            checkInstanceAndDeleteIfNeeded(onDataLost)
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

        /*
        ************************************************************************************************
        ** Private fun
        ************************************************************************************************
        */
        private fun checkInstanceAndDeleteIfNeeded(onDataLost: () -> Unit) {
            try {
                val dynamicRealm = DynamicRealm.getInstance(defaultConfiguration)
                val shouldMigrate = dynamicRealm.version < DB_VERSION
                dynamicRealm.close()
                if (shouldMigrate) {
                    dropDb(onDataLost)
                }
            } catch (ex: Exception) {
                logE(TAG, { "init > fail" }, ex)
                dropDb(onDataLost)
            }
        }

        private fun dropDb(onDataLost: () -> Unit) {
            Realm.deleteRealm(defaultConfiguration)
            onDataLost()
        }
    }
}