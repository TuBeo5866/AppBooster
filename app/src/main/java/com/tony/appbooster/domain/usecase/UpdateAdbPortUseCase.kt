package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Updates the ADB server TCP port based on user input so that subsequent ADB
 * sessions can reach custom ADB instances or non-default endpoints.
 *
 * @param settingsRepository Repository used to persist the ADB port value.
 */
class UpdateAdbPortUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Persists the provided ADB port into settings for future sessions.
     *
     * @param port TCP port that the ADB server listens on.
     * @return [Resource] indicating success or failure of the persistence.
     */
    suspend operator fun invoke(
        port: Int
    ): Resource<Unit> = settingsRepository.setAdbPort(port)
}
