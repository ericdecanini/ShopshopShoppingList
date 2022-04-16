package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter


@BindingAdapter("app:clearsFocusOnClick")
fun View.clearsFocusOnClick(clearsFocus: Boolean) {
    if (clearsFocus)
        setOnTouchListener { _, _ ->
            rootView.findFocus()?.clearFocus()
            hideKeyboard(this)
            performClick()
            true
        }
}

private fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
