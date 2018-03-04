package com.saveurmarche.saveurmarche.data.preference

import android.content.SharedPreferences

class SaveurPreferenceManager(private val preference: SharedPreferences) {
    companion object {
        const val LAST_JSON_FETCH_KEY = "SaveurPreferenceManager.LAST_JSON_FETCH_KEY"
        const val OAUTH_LOGGED_IN = "SaveurPreferenceManager.OAUTH_LOGGED_IN"
        const val OAUTH_ACCESS_TOKEN = "SaveurPreferenceManager.OAUTH_ACCESS_TOKEN"
        const val OAUTH_REFRESH_TOKEN = "SaveurPreferenceManager.OAUTH_REFRESH_TOKEN"
    }

    var lastJsonFetchData: Int
        get() = preference.getInt(LAST_JSON_FETCH_KEY, -1)
        set(value) {
            preference.edit().putInt(LAST_JSON_FETCH_KEY, value).apply()
        }

    var oauthLoggedIn: Boolean
        get() = preference.getBoolean(OAUTH_LOGGED_IN, false)
        set(value) {
            preference.edit().putBoolean(OAUTH_LOGGED_IN, value).apply()
        }

    var oauthAccessToken: String?
        get() = preference.getString(OAUTH_ACCESS_TOKEN, null)
        set(value) {
            preference.edit().putString(OAUTH_ACCESS_TOKEN, value).apply()
        }

    var oauthRefreshToken: String?
        get() = preference.getString(OAUTH_REFRESH_TOKEN, null)
        set(value) {
            preference.edit().putString(OAUTH_REFRESH_TOKEN, value).apply()
        }
}