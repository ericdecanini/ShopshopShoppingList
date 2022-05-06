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
        positiveOnClick: ((String) -> Unit),
        negativeOnClick: (() -> Unit)? = null,
        cancellable: Boolean = true,
    )

    fun displayThemeDialog(onThemeSelected: (Theme) -> Unit)
}
