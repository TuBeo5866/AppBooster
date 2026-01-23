package com.tony.appbooster.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.common.ResourceError
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val SETTINGS_DATA_STORE_NAME = "app_settings"
private const val DEFAULT_ADB_HOST = "127.0.0.1"
private const val DEFAULT_ADB_PORT = -1
private const val DEFAULT_ADB_PAIRING_CODE = -1


private val Context.settingsDataStore by preferencesDataStore(
    name = SETTINGS_DATA_STORE_NAME
)

/**
 * DataStore-backed implementation of [SettingsRepository] that persists user
 * configuration such as optimization mode and ADB connection details, exposing
 * them as [Resource]-wrapped domain objects.
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
        val ADB_HOST: Preferences.Key<String> =
            stringPreferencesKey("adb_host")
        val ADB_PORT: Preferences.Key<Int> =
            intPreferencesKey("adb_port")
        val ADB_PAIRING_CODE: Preferences.Key<Int> =
            intPreferencesKey("adb_pairing_code")
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

    override fun observeAdbHost(): Flow<Resource<String>> {
        return applicationContext.settingsDataStore
            .data
            .map { preferences ->
                val host = preferences[Keys.ADB_HOST] ?: DEFAULT_ADB_HOST
                Resource.Success(host) as Resource<String>
            }
            .catch { throwable ->
                emit(
                    Resource.Error(
                        ResourceError.DatabaseError(
                            message = throwable.message ?: "Unable to read ADB host"
                        )
                    )
                )
            }
    }

    override suspend fun setAdbHost(
        host: String
    ): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.ADB_HOST] = host.ifBlank { DEFAULT_ADB_HOST }
            }
            Resource.Success(Unit)
        } catch (throwable: Throwable) {
            Resource.Error(
                ResourceError.DatabaseError(
                    message = throwable.message ?: "Unable to persist ADB host"
                )
            )
        }
    }

    override fun observeAdbPort(): Flow<Resource<Int>> {
        return applicationContext.settingsDataStore
            .data
            .map { preferences ->
                val port = preferences[Keys.ADB_PORT] ?: DEFAULT_ADB_PORT
                Resource.Success(port) as Resource<Int>
            }
            .catch { throwable ->
                emit(
                    Resource.Error(
                        ResourceError.DatabaseError(
                            message = throwable.message ?: "Unable to read ADB port"
                        )
                    )
                )
            }
    }

    override suspend fun setAdbParingCode(
        code: Int
    ): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.ADB_PAIRING_CODE] = code
            }
            Resource.Success(Unit)
        } catch (throwable: Throwable) {
            Resource.Error(
                ResourceError.DatabaseError(
                    message = throwable.message ?: "Unable to persist ADB Pairing code"
                )
            )
        }
    }

    override fun observeAdbPairingCode(): Flow<Resource<Int>> {
        return applicationContext.settingsDataStore
            .data
            .map { preferences ->
                val port = preferences[Keys.ADB_PAIRING_CODE] ?: DEFAULT_ADB_PAIRING_CODE
                Resource.Success(port) as Resource<Int>
            }
            .catch { throwable ->
                emit(
                    Resource.Error(
                        ResourceError.DatabaseError(
                            message = throwable.message ?: "Unable to read ADB port"
                        )
                    )
                )
            }
    }

    override suspend fun setAdbPort(
        port: Int
    ): Resource<Unit> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[Keys.ADB_PORT] = if (port > 0) port else DEFAULT_ADB_PORT
            }
            Resource.Success(Unit)
        } catch (throwable: Throwable) {
            Resource.Error(
                ResourceError.DatabaseError(
                    message = throwable.message ?: "Unable to persist ADB port"
                )
            )
        }
    }
}
