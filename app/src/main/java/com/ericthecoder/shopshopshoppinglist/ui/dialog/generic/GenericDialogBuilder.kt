package com.ericthecoder.shopshopshoppinglist.ui.dialog.generic

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object GenericDialogBuilder {

    fun show(
        activity: Activity,
        title: String?,
        message: String?,
        positiveButton: Pair<String, (() -> Unit)?>?,
        negativeButton: Pair<String, (() -> Unit)?>?,
        cancellable: Boolean,
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancellable)
            .setPositiveButton(positiveButton?.first) { _, _ -> positiveButton?.second?.invoke() }
            .setNegativeButton(negativeButton?.first) { _, _ -> negativeButton?.second?.invoke() }
            .show()
    }
}