package com.ericthecoder.shopshopshoppinglist.ui.snackbar

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

interface SnackbarNavigator {

    fun displaySnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT)

    fun displaySnackbar(@StringRes stringRes: Int, length: Int = Snackbar.LENGTH_SHORT)

}
