package com.tony.appbooster.domain.usecase.shizuku

import com.tony.appbooster.domain.client.ShizukuShellClient
import javax.inject.Inject

/**
 * Opens the Shizuku installation page for first-time setup.
 *
 * @property shizukuClient Client that can open external Shizuku destinations.
 */
class OpenShizukuInstallPageUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Opens the install page in browser or Play Store.
     */
    operator fun invoke() = shizukuClient.openShizukuInstallPage()
}

