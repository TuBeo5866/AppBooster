package com.tony.appbooster.presentation.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Determines whether the runtime notification permission is required and granted.
 *
 * Business purpose: WorkManager runs the optimization in the foreground, which relies on
 * a visible notification. On Android 13+ this requires POST_NOTIFICATIONS to be granted.
 */
object NotificationPermissionManager {

    /**
     * Returns true if the app should ask the user for POST_NOTIFICATIONS.
     *
     * @param context Context used to check current permission state.
     */
    fun shouldRequest(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    }
}
