package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.ericthecoder.shopshopshoppinglist.util.listeners.UiEventListeners

@BindingAdapter(value = ["app:onChecked", "app:clearsFocusOnCheck"], requireAll = false)
fun CheckBox.onChecked(
    callback: UiEventListeners.OnCheckChangedListener,
    clearsFocus: Boolean = false,
) {
    setOnCheckedChangeListener { _, _ ->
        if (clearsFocus)
            rootView.findFocus()?.clearFocus()
        callback.onCheckChanged(this)
    }
}
