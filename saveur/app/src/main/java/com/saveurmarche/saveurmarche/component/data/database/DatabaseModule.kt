package com.saveurmarche.saveurmarche.component.data.database

import com.saveurmarche.saveurmarche.api.SaveurServices
import com.saveurmarche.saveurmarche.component.data.DataScope
import com.saveurmarche.saveurmarche.component.data.network.NetworkModule
import com.saveurmarche.saveurmarche.data.manager.MarketsManager
import com.saveurmarche.saveurmarche.data.network.SaveurRequestManager
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    /**
     * @param service provided by [NetworkModule.provideApiService]
     */
    @DataScope
    @Provides
    fun provideRequestManager(service: SaveurServices.ApiService): SaveurRequestManager {
        return SaveurRequestManager(service)
    }

    /**
     * @param requestManager provided by [provideRequestManager]
     */
    @DataScope
    @Provides
    fun provideMarketManager(requestManager: SaveurRequestManager, preferenceManager: SaveurPreferenceManager): MarketsManager {
        return MarketsManager(requestManager, preferenceManager)
    }
}