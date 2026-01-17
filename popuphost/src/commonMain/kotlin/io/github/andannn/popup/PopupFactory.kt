/*
 * Copyright 2025, the PopupHost project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.andannn.popup

import androidx.compose.runtime.Composable

interface PopupFactory {
    @Composable
    fun PopupFactoryScope.Content()
}

data class PopupFactoryScope(
    val onRequestDismiss: () -> Unit,
    val onPerformAction: (Any) -> Unit,
)

interface PopupFactoryProvider {
    fun create(entry: PopupEntry<*>): PopupFactory?
}
