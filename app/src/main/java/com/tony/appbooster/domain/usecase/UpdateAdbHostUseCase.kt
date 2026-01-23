package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Updates the ADB server host based on user input so that subsequent ADB
 * sessions can connect to the desired machine or emulator instance.
 *
 * @param settingsRepository Repository used to persist the ADB host value.
 */
class UpdateAdbHostUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Persists the provided ADB host name into settings for future sessions.
     *
     * @param host Hostname or IP address of the ADB server.
     * @return [Resource] indicating success or failure of the persistence.
     */
    suspend operator fun invoke(
        host: String
    ): Resource<Unit> = settingsRepository.setAdbHost(host)
}
