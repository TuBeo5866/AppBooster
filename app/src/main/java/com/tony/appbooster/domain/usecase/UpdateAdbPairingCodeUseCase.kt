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
class UpdateAdbPairingCodeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Persists the provided ADB port into settings for future sessions.
     *
     * @param code paring code
     * @return [Resource] indicating success or failure of the persistence.
     */
    suspend operator fun invoke(
        code: Int
    ): Resource<Unit> = settingsRepository.setAdbParingCode(code)
}
