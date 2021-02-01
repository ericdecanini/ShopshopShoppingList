package com.ericdecanini.shopshopshoppinglist.library.bindingadapter

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.ericdecanini.shopshopshoppinglist.util.UiEventListeners

@BindingAdapter("app:onChecked")
fun CheckBox.onChecked(callback: UiEventListeners.OnCheckChangedListener) {
    setOnCheckedChangeListener { _, _ ->
        callback.onCheckChanged(this)
    }
}
