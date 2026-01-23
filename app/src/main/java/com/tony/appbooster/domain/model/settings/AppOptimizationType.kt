package com.tony.appbooster.domain.model.settings

/**
 * Represents how the application should optimize its runtime behavior,
 * allowing the user to trade off compilation time against runtime speed.
 */
enum class AppOptimizationType(val value: String) {
    /**
     * Prioritizes fast install and incremental builds over maximum runtime speed.
     */
    SPEED_PROFILE("speed-profile"),

    /**
     * Compiles more aggressively for runtime performance at the cost of longer build times.
     */
    FULL_OPTIMIZATION("speed");
}
