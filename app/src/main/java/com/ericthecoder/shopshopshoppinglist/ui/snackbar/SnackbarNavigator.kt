package com.ericthecoder.shopshopshoppinglist.ui.snackbar

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

interface SnackbarNavigator {

    fun displaySnackbar(
        message: String,
        ctaText: String? = null,
        ctaCallback: (() -> Unit)? = null,
        length: Int = Snackbar.LENGTH_SHORT,
    )

    fun displaySnackbar(
        @StringRes stringRes: Int,
        ctaText: String? = null,
        ctaCallback: (() -> Unit)? = null,
        length: Int = Snackbar.LENGTH_SHORT,
    )
}
