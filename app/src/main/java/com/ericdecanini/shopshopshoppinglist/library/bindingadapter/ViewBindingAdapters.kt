package com.ericdecanini.shopshopshoppinglist.library.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:clearsFocusOnClick")
fun View.clearsFocusOnClick(clearsFocus: Boolean) {
    if (clearsFocus)
        setOnTouchListener { _, _ ->
            rootView.findFocus()?.clearFocus()
            performClick()
            true
        }
}
