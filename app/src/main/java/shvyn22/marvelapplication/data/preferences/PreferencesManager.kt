package shvyn22.marvelapplication.data.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class Prefs(var nightMode: Int)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
){
    private val dataStore = context.createDataStore("userPrefs")

    val preferencesFlow = dataStore.data.map {
        val nightMode = it[PreferencesKeys.NIGHT_MODE] ?: AppCompatDelegate.getDefaultNightMode()
        Prefs(nightMode)
    }

    suspend fun updateNightMode(nightMode: Int) {
        dataStore.edit {
            it[PreferencesKeys.NIGHT_MODE] = nightMode
        }
    }

    private object PreferencesKeys {
        val NIGHT_MODE = preferencesKey<Int>("nightMode")
    }
}