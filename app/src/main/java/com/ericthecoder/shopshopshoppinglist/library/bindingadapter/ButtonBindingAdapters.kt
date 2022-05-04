package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isSelected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}