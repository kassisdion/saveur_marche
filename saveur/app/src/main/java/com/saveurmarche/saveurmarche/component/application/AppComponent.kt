package com.saveurmarche.saveurmarche.component.application

import android.content.Context
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun application(): SaveurApplication

    fun context(): Context

    fun pumpkinPreferencesManager(): SaveurPreferenceManager
}