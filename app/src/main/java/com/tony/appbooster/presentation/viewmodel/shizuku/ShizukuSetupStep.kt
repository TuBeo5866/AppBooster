package com.tony.appbooster.presentation.viewmodel.shizuku

/**
 * Discrete steps in the Shizuku setup wizard.
 *
 * Each variant corresponds to a screen card shown in the Shizuku setup screen
 * and drives the progress indicator and hero section animations.
 */
enum class ShizukuSetupStep {

    /** Initial status check before determining which step to show. */
    CHECK_STATUS,

    /** Shizuku needs to be installed from the Play Store or direct APK. */
    INSTALL_SHIZUKU,

    /** Shizuku is installed but its background service has not been started. */
    START_SERVICE,

    /** Service is running but the app has not been granted Shizuku permission yet. */
    GRANT_PERMISSION,

    /** All prerequisites satisfied; the app is ready to perform privileged operations. */
    READY
}

