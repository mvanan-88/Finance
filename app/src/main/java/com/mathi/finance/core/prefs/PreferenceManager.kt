package com.mathi.finance.core.prefs

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("my_finance_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", -1)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
