package com.saveurmarche.saveurmarche.component.data

import com.saveurmarche.saveurmarche.api.Services
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
    fun provideRequestManager(service: Services.ApiService, preferenceManager: SaveurPreferenceManager): SaveurRequestManager {
        return SaveurRequestManager(service, preferenceManager)
    }

    /**
     * @param requestManager provided by [provideRequestManager]
     */
    @DataScope
    @Provides
    fun provideMarketManager(requestManager: SaveurRequestManager): MarketsManager {
        return MarketsManager(requestManager)
    }
}