package com.example.schedapp.presentation.screen.common.basescreen

data class ErrorDialogConfig(
    val onConfirm: () -> Unit = {},
    val onCancel: (() -> Unit)? = null,
    val onDismissRequest: (() -> Unit)? = null,
    val confirmButtonText: String? = null,
    val retryButtonText: String? = null,
    val dismissButtonText: String? = null
)
