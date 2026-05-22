package com.eam.parqueaventuraapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.favoritosDataStore: DataStore<Preferences> by preferencesDataStore(name = "favoritos_prefs")

class FavoritosRepositorio(context: Context) {

    private val dataStore = context.favoritosDataStore

    companion object {
        private val FAVORITOS_KEY = stringSetPreferencesKey("favoritos_ids")
    }

    val favoritosFlow: Flow<Set<String>> = dataStore.data
        .map { prefs -> prefs[FAVORITOS_KEY] ?: emptySet() }

    suspend fun agregarFavorito(id: String) {
        dataStore.edit { prefs ->
            val current = prefs[FAVORITOS_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(id)
            prefs[FAVORITOS_KEY] = current
        }
    }

    suspend fun eliminarFavorito(id: String) {
        dataStore.edit { prefs ->
            val current = prefs[FAVORITOS_KEY]?.toMutableSet() ?: mutableSetOf()
            current.remove(id)
            prefs[FAVORITOS_KEY] = current
        }
    }

    suspend fun setFavoritos(ids: Set<String>) {
        dataStore.edit { prefs ->
            prefs[FAVORITOS_KEY] = ids
        }
    }
}
