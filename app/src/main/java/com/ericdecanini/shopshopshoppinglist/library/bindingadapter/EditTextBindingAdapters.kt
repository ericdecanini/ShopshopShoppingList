package com.ericdecanini.shopshopshoppinglist.library.bindingadapter

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("app:returnKeyClick")
fun EditText.returnKeyClickView(view: View) {
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
            view.performClick()
        else
            false
    }
}
