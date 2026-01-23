package com.tony.appbooster.presentation.viewmodel.base

import android.content.Context
import androidx.annotation.StringRes
import com.tony.appbooster.presentation.viewmodel.base.AppBoosterStringProvider.init

/**
 * Provides localized string resources from a process-wide [Context].
 *
 * Business purpose:
 * - Allows non-UI layers inside the presentation module (e.g., a [BaseViewModel])
 *   to resolve `R.string.*` ids without holding onto an Activity/Fragment reference.
 * - Keeps all user-visible text centralized in Android resources for localization.
 *
 * Thread-safety:
 * - Initialization is idempotent and should be called once from the application.
 * - Reads are lock-free after initialization.
 */
internal object AppBoosterStringProvider {

    @Volatile
    private var appContext: Context? = null

    /**
     * Initializes the provider with a long-lived application [Context].
     *
     * @param context Any context; it will be converted to `applicationContext`.
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /**
     * Returns a localized string for the provided resource id.
     *
     * @param id The string resource id.
     * @param formatArgs Optional formatting arguments for placeholders.
     * @return The resolved localized string.
     * @throws IllegalStateException If [init] wasn't called before use.
     */
    fun get(@StringRes id: Int, vararg formatArgs: Any): String {
        val ctx = appContext ?: error("AppBoosterStringProvider is not initialized. Call init(context) from Application.")
        return if (formatArgs.isEmpty()) {
            ctx.getString(id)
        } else {
            ctx.getString(id, *formatArgs)
        }
    }
}
