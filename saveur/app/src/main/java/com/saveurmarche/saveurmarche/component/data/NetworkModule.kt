package com.saveurmarche.saveurmarche.component.data

import com.saveurmarche.saveurmarche.BuildConfig
import com.saveurmarche.saveurmarche.api.SaveurAuthenticator
import com.saveurmarche.saveurmarche.api.Services
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
    fun provideOauthService(): Services.OauthService {
        val client = OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        interceptors().add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .build()

        val converterFactory = MoshiConverterFactory.create(Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build())

        val callAdapterFactory = RxJava2CallAdapterFactory.create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create(Services.OauthService::class.java)
    }

    /*
    ************************************************************************************************
    ** SAVEUR API
    ************************************************************************************************
     */
    @DataScope
    @Provides
    fun provideApiService(preferenceManager: SaveurPreferenceManager, oauthService: Services.OauthService): Services.ApiService {
        val client = OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        interceptors().add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .authenticator(SaveurAuthenticator(oauthService, preferenceManager))
                .build()

        val converterFactory = MoshiConverterFactory.create(Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build())

        val callAdapterFactory = RxJava2CallAdapterFactory.create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create(Services.ApiService::class.java)
    }
}