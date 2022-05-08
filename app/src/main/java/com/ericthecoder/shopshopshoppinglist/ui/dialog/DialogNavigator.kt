package com.ericthecoder.shopshopshoppinglist.ui.dialog

import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme

interface DialogNavigator {

    fun displayGenericDialog(
        title: String? = null,
        message: String? = null,
        positiveButton: Pair<String, (() -> Unit)?>? = null,
        negativeButton: Pair<String, (() -> Unit)?>? = null,
        cancellable: Boolean = true,
    )

    fun displayRenameDialog(
        header: String,
        autofillText: String,
        positiveButton: Pair<String, (String) -> Unit>,
        negativeButton: Pair<String, () -> Unit>? = null,
        cancellable: Boolean = true,
    )

    fun displayThemeDialog(onThemeSelected: (Theme) -> Unit)
}
