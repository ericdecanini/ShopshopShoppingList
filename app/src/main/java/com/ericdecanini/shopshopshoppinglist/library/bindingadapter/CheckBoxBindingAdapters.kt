package com.ericdecanini.shopshopshoppinglist.library.bindingadapter

import android.view.View
import android.widget.CheckBox
import androidx.databinding.BindingAdapter

@BindingAdapter("app:onChecked")
fun CheckBox.onChecked(callback: View.OnClickListener) {
    setOnCheckedChangeListener { view, _ ->
        callback.onClick(view)
    }
}
