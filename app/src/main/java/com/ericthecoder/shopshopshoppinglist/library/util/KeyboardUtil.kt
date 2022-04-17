package com.ericthecoder.shopshopshoppinglist.library.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun hideKeyboard(view: View) {
    val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}
