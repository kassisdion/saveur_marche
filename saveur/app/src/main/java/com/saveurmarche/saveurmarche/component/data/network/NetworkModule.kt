package com.saveurmarche.saveurmarche.component.data.network

import com.saveurmarche.saveurmarche.BuildConfig
import com.saveurmarche.saveurmarche.api.SaveurAuthenticator
import com.saveurmarche.saveurmarche.api.SaveurInterceptor
import com.saveurmarche.saveurmarche.api.SaveurServices
import com.saveurmarche.saveurmarche.component.data.DataScope
import com.saveurmarche.saveurmarche.data.preference.SaveurPreferenceManager
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
    /*
    ************************************************************************************************
    ** OAUTH API
    ************************************************************************************************
     */
    @DataScope
    @Provides
    fun provideOauthService(): SaveurServices.OauthService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(OkHttpClient.Builder()
                        .apply {
                            //Add HttpLoggingInterceptor (never add it in production code)
                            if (BuildConfig.DEBUG) {
                                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            }
                        }
                        .build())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SaveurServices.OauthService::class.java)
    }

    /*
    ************************************************************************************************
    ** SAVEUR API
    ************************************************************************************************
     */
    @DataScope
    @Provides
    fun provideApiService(preferenceManager: SaveurPreferenceManager, oauthService: SaveurServices.OauthService): SaveurServices.ApiService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(OkHttpClient.Builder()
                        .apply {
                            //Add HttpLoggingInterceptor (never add it in production code)
                            if (BuildConfig.DEBUG) {
                                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            }

                            addInterceptor(SaveurInterceptor(preferenceManager))
                            authenticator(SaveurAuthenticator(oauthService, preferenceManager))
                        }
                        .build())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SaveurServices.ApiService::class.java)
    }
}