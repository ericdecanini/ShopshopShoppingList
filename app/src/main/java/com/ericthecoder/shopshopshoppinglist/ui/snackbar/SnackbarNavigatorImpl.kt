package com.ericthecoder.shopshopshoppinglist.ui.snackbar

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.library.extension.getRootView
import com.google.android.material.snackbar.Snackbar

class SnackbarNavigatorImpl(
    private val activity: AppCompatActivity,
    private val resourceProvider: ResourceProvider,
) : SnackbarNavigator {

    override fun displaySnackbar(message: String, length: Int) {
        activity.runOnUiThread {
            Snackbar.make(activity.getRootView(), message, length).show()
        }
    }

    override fun displaySnackbar(stringRes: Int, length: Int) {
        displaySnackbar(resourceProvider.getString(stringRes), length)
    }
}
