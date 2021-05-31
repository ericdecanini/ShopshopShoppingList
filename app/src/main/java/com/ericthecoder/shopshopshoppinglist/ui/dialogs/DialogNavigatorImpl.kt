package com.ericthecoder.shopshopshoppinglist.ui.dialogs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic.GenericDialogFragment
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename.RenameDialogFragment
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider

class DialogNavigatorImpl(private val topActivityProvider: TopActivityProvider) : DialogNavigator {

    private val activity get() = topActivityProvider.getTopActivity()
        ?: throw NullPointerException()

    override fun displayGenericDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        positiveOnClick: (() -> Unit)?,
        negativeText: String?,
        negativeOnClick: (() -> Unit)?,
        cancellable: Boolean
    ) {
        val builder = GenericDialogFragment.Builder(activity)
            .setCancellable(cancellable)
        title?.let { builder.setTitle(it) }
        message?.let { builder.setMessage(it) }
        positiveText?.let { builder.setPositiveButton(it, positiveOnClick) }
        negativeText?.let { builder.setNegativeButton(it, negativeOnClick) }

        val fragment = builder.create()
        fragment.showAllowingStateLoss(DIALOG_TAG)
    }

    override fun displayRenameDialog(
        listTitle: String,
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
        cancellable: Boolean
    ) {
        val builder = RenameDialogFragment.Builder(activity)
            .setListTitle(listTitle)
            .setPositiveOnClick(positiveOnClick)
            .setCancellable(cancellable)
        negativeOnClick?.let { builder.setNegativeOnClick(it) }

        val fragment = builder.create()
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
