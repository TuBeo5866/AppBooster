package com.tony.appbooster.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.common.ResourceError
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val SETTINGS_DATA_STORE_NAME = "app_settings"


private val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATA_STORE_NAME
)

/**
 * DataStore-backed implementation of [SettingsRepository] that persists user
 * configuration.
 *
 * The app currently persists only the selected [AppOptimizationType]. Legacy
 * ADB host/port/pairing-code configuration has been removed.
 *
 * @param applicationContext Application-level [Context] used to access DataStore.
 */
@Singleton
class SettingsDataStoreRepository @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context
) : SettingsRepository {

    private object Keys {
        val APP_OPTIMIZATION_TYPE: Preferences.Key<String> =
            stringPreferencesKey("app_optimization_type")
    }

    override fun observeAppOptimizationType(): Flow<Resource<AppOptimizationType>> {
        return applicationContext.settingsDataStore
            .data
            .map { preferences ->
                val rawValue = preferences[Keys.APP_OPTIMIZATION_TYPE]
                val type = rawValue
                    ?.let { stored ->
                        runCatching { AppOptimizationType.valueOf(stored) }
                            .getOrDefault(AppOptimizationType.SPEED_PROFILE)
                    }
                    ?: AppOptimizationType.SPEED_PROFILE

                Resource.Success(type) as Resource<AppOptimizationType>
            }
            .catch { throwable ->
                emit(
                    Resource.Error(
                        ResourceError.DatabaseError(
                            message = throwable.message ?: "Unable to read optimization type"
                        )
                    )
                )
            }
    }

    override suspend fun setAppOptimizationType(
        type: AppOptimizationType
    ): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.APP_OPTIMIZATION_TYPE] = type.name
            }
            Resource.Success(Unit)
        } catch (throwable: Throwable) {
            Resource.Error(
                ResourceError.DatabaseError(
                    message = throwable.message ?: "Unable to persist optimization type"
                )
            )
        }
    }
}
