package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:emptyAreaFocusable")
fun RecyclerView.emptyAreaFocusable(focusable: Boolean) {
    if (focusable) {
        setOnTouchListener { _, _ ->
            clearFocus()
            performClick()
            false
        }
    }
}
