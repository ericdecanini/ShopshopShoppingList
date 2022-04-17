package com.ericthecoder.shopshopshoppinglist.ui.snackbar

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.library.extension.getRootView
import com.google.android.material.snackbar.Snackbar

class SnackbarNavigatorImpl(
    private val activity: AppCompatActivity,
    private val resourceProvider: ResourceProvider,
) : SnackbarNavigator {

    override fun displaySnackbar(
        message: String,
        ctaText: String?,
        ctaCallback: (() -> Unit)?,
        length: Int,
    ) {
        activity.runOnUiThread {
            val snackbar = Snackbar.make(activity.getRootView(), message, length)
            if (!ctaText.isNullOrEmpty()) {
                snackbar.setAction(ctaText) { ctaCallback?.invoke() }
            }
            snackbar.show()
        }
    }

    override fun displaySnackbar(
        stringRes: Int,
        ctaText: String?,
        ctaCallback: (() -> Unit)?,
        length: Int,
    ) {
        val message = resourceProvider.getString(stringRes)
        displaySnackbar(message, ctaText, ctaCallback, length)
    }
}
