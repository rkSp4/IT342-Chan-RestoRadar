package com.chan.restoradar.shared.network

import android.content.Context
import android.content.SharedPreferences
import com.chan.restoradar.shared.data.UserProfile
import com.google.gson.Gson

object SessionStore {

    private const val PREFS_NAME  = "restoradar_prefs"
    private const val KEY_TOKEN   = "restoradar_access_token"
    private const val KEY_REFRESH = "restoradar_refresh_token"
    private const val KEY_USER    = "restoradar_user"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(context: Context, accessToken: String, refreshToken: String) {
        prefs(context).edit()
            .putString(KEY_TOKEN, accessToken)
            .putString(KEY_REFRESH, refreshToken)
            .apply()
    }

    fun saveUser(context: Context, user: UserProfile) {
        prefs(context).edit()
            .putString(KEY_USER, Gson().toJson(user))
            .apply()
    }

    fun getToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)

    fun getUser(context: Context): UserProfile? {
        val json = prefs(context).getString(KEY_USER, null) ?: return null
        return Gson().fromJson(json, UserProfile::class.java)
    }

    fun getUserId(context: Context): String? = getUser(context)?.id

    fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean = getToken(context) != null
}