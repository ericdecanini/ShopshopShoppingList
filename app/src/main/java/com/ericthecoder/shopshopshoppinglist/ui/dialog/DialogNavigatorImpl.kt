package com.ericthecoder.shopshopshoppinglist.ui.dialog

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.ericthecoder.shopshopshoppinglist.ui.dialog.generic.GenericDialogBuilder
import com.ericthecoder.shopshopshoppinglist.ui.dialog.rename.RenameDialogBuilder
import com.ericthecoder.shopshopshoppinglist.ui.dialog.theme.ThemeDialogBuilder
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader

class DialogNavigatorImpl(
    private val activity: AppCompatActivity,
    private val persistentStorageReader: PersistentStorageReader,
    ) : DialogNavigator {

    override fun displayGenericDialog(
        title: String?,
        message: String?,
        positiveButton: Pair<String, (() -> Unit)?>?,
        negativeButton: Pair<String, (() -> Unit)?>?,
        cancellable: Boolean,
    ) {
        GenericDialogBuilder.show(
            activity, title, message, positiveButton, negativeButton, cancellable
        )
    }

    override fun displayRenameDialog(
        header: String,
        autofillText: String,
        positiveButton: Pair<String, (String) -> Unit>,
        negativeButton: Pair<String, () -> Unit>?,
        cancellable: Boolean,
    ) {
        RenameDialogBuilder.show(
            activity, header, autofillText, positiveButton, negativeButton, cancellable
        )
    }

    override fun displayThemeDialog(onThemeSelected: (Theme) -> Unit) {
        val currentTheme =  Theme.valueOf(persistentStorageReader.getCurrentTheme())
        ThemeDialogBuilder.show(activity, currentTheme, onThemeSelected)
    }
}
