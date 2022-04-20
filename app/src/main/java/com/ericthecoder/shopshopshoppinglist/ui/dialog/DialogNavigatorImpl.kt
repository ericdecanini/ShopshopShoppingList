package com.ericthecoder.shopshopshoppinglist.ui.dialog

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.theme.ThemeViewModel
import com.ericthecoder.shopshopshoppinglist.ui.dialog.generic.GenericDialogBuilder
import com.ericthecoder.shopshopshoppinglist.ui.dialog.rename.RenameDialogBuilder

class DialogNavigatorImpl(
    private val activity: AppCompatActivity,
    private val themeViewModel: ThemeViewModel,
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
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
        cancellable: Boolean,
    ) {
        RenameDialogBuilder.show(
            activity, header, autofillText, positiveOnClick, negativeOnClick, cancellable
        )
    }
}
