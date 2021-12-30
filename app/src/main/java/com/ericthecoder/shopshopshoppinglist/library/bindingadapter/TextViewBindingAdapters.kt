package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:drawableStart")
fun TextView.drawableStart(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}
