package com.ericthecoder.shopshopshoppinglist.ui.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ericthecoder.shopshopshoppinglist.theme.ThemeViewModel
import com.ericthecoder.shopshopshoppinglist.ui.dialog.generic.GenericDialogFragment
import com.ericthecoder.shopshopshoppinglist.ui.dialog.rename.RenameDialogFragment

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
        val builder = GenericDialogFragment.Builder(activity)
            .setCancellable(cancellable)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton?.first, positiveButton?.second)
        builder.setNegativeButton(negativeButton?.first, negativeButton?.second)

        val fragment = builder.create(theme = themeViewModel.getTheme())
        fragment.showAllowingStateLoss(DIALOG_TAG)
    }

    override fun displayRenameDialog(
        header: String,
        autofillText: String,
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
        cancellable: Boolean,
    ) {
        val builder = RenameDialogFragment.Builder()
            .setHeader(header)
            .setAutofillText(autofillText)
            .setPositiveOnClick(positiveOnClick)
            .setNegativeOnClick(negativeOnClick)
            .setCancellable(cancellable)

        val fragment = builder.build(themeViewModel.getTheme())
        fragment.showAllowingStateLoss(DIALOG_TAG)
    }

    private fun DialogFragment.showAllowingStateLoss(tag: String) =
        this@DialogNavigatorImpl.activity.supportFragmentManager.showDialogAllowingStateLoss(this, tag)

    private fun FragmentManager.showDialogAllowingStateLoss(dialogFragment: DialogFragment, tag: String) {
        beginTransaction().add(dialogFragment, tag).commitAllowingStateLoss()
    }

    companion object {
        internal const val DIALOG_TAG = "DIALOG_TAG"
    }
}
