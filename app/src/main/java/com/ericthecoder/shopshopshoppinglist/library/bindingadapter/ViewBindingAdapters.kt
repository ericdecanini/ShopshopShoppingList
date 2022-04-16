package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.ericthecoder.shopshopshoppinglist.library.util.hideKeyboard

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