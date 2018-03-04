package com.saveurmarche.saveurmarche.component.application

import android.content.Context
import com.saveurmarche.saveurmarche.SaveurApplication
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: SaveurApplication) {
    companion object {
        const val PREFERENCE_NAME = "SaveurMarchePreference"
    }

    /*
    ************************************************************************************************
    ** Public dependencies for injection
    ************************************************************************************************
     */
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication(): SaveurApplication = app

    @Provides
    @Singleton
    fun provideSharedPreference(app: SaveurApplication) = SaveurPreferenceManager(app.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE))
}