package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ericthecoder.shopshopshoppinglist.util.listeners.UiEventListeners

@BindingAdapter(value = ["app:emptyAreaFocusable", "app:onFocusGained"], requireAll = false)
fun RecyclerView.emptyAreaFocusable(focusable: Boolean, onFocusGainedListener: UiEventListeners.OnFocusGainedListener) {
    if (focusable) {
        setOnTouchListener { _, _ ->
            clearFocus()
            performClick()
            onFocusGainedListener.onFocusGained(this)
            false
        }
    }
}
