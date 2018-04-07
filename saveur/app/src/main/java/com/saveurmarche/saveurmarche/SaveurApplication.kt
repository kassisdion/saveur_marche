package com.saveurmarche.saveurmarche

import android.app.Application
import android.content.Context
import com.saveurmarche.saveurmarche.component.application.AppComponent
import com.saveurmarche.saveurmarche.component.application.AppModule
import com.saveurmarche.saveurmarche.component.application.DaggerAppComponent
import com.saveurmarche.saveurmarche.component.data.DaggerDataComponent
import com.saveurmarche.saveurmarche.component.data.DataComponent
import com.saveurmarche.saveurmarche.helper.realm.RealmHelper
import com.akaita.java.rxjava2debug.RxJava2Debug

class SaveurApplication : Application() {
    /*
    ************************************************************************************************
    ** Singleton
    ************************************************************************************************
    */
    /**
     * Simple singleton used to access to [AppComponent] instance needed for resolving injection
     */
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var dataComponent: DataComponent

        operator fun get(context: Context): SaveurApplication {
            return context.applicationContext as SaveurApplication
        }
    }

    /*
    ************************************************************************************************
    ** Life cycle
    ************************************************************************************************
    */
    override fun onCreate() {
        super.onCreate()
        init()
    }

    /*
    ************************************************************************************************
    ** Private fun
    ************************************************************************************************
    */
    private fun init() {
        setupComponent()
        setupRealm()
        setupRxDebug()
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()

        dataComponent = DaggerDataComponent
                .builder()
                .appComponent(appComponent)
                .build()
    }

    private fun setupRealm() {
        RealmHelper.init(this, { onDbDropped() })
    }

    private fun setupRxDebug() {
        // Enable RxJava assembly stack collection, to make RxJava crash reports clear and unique
        // Make sure this is called AFTER setting up any Crash reporting mechanism as Crashlytics
        RxJava2Debug.enableRxJava2AssemblyTracking()
    }

    private fun onDbDropped() {
        //We've lost every data so wee need to reset the lastJsonFetchData
        appComponent.pumpkinPreferencesManager().lastJsonFetchData = -1
    }
}