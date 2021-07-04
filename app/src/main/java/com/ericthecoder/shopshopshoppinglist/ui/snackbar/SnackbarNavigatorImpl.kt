package com.ericthecoder.shopshopshoppinglist.ui.snackbar

import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.library.extension.getRootView
import com.google.android.material.snackbar.Snackbar

class SnackbarNavigatorImpl(
    private val topActivityProvider: TopActivityProvider,
    private val resourceProvider: ResourceProvider,
) : SnackbarNavigator {

    private val activity get() = topActivityProvider.getTopActivity()
        ?: throw NullPointerException()

    override fun displaySnackbar(message: String, length: Int) {
        activity.runOnUiThread {
            Snackbar.make(activity.getRootView(), message, length).show()
        }
    }

    override fun displaySnackbar(stringRes: Int, length: Int) {
        displaySnackbar(resourceProvider.getString(stringRes), length)
    }
}
