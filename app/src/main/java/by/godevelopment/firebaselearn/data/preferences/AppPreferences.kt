package by.godevelopment.firebaselearn.data.preferences

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class AppPreferences @Inject constructor(appContext: Context) {

    private val sharedPreferences = appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setCurrentAccountId(partyId: Long) {
        sharedPreferences.edit {
            putLong(PREF_CURRENT_KEY, partyId)
        }
    }

    fun getCurrentAccountId(): Long = sharedPreferences.getLong(PREF_CURRENT_KEY, NO_KEY)

    companion object {
        private const val PREFERENCE_NAME = "app_pref"
        private const val PREF_CURRENT_KEY = "current_data"
        const val NO_KEY = -1L
    }
}
