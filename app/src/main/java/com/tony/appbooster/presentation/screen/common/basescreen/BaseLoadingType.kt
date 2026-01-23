package com.tony.appbooster.presentation.screen.common.basescreen

enum class BaseLoadingType{

    /**
     * Displays only the loading indicator, completely hiding the underlying content.
     * Use this when the content should not be visible until loading is complete.
     */
    DEFAULT,

    /**
     * Displays the loading indicator on top of the content with a semi-transparent overlay.
     * The content remains visible but is visually deemphasized, typically indicating it is inactive.
     */
    OVERLAY,

    /**
     * No loading indicator is shown. The content remains fully visible and interactive.
     */
    NONE
}