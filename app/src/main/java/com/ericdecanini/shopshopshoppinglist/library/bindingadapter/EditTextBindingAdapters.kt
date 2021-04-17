package com.ericdecanini.shopshopshoppinglist.library.bindingadapter

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.ericdecanini.shopshopshoppinglist.util.UiEventListeners

@BindingAdapter("app:returnKeyClick")
fun EditText.returnKeyClickView(view: View) {
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
            view.performClick()
        else
            false
    }
}

@BindingAdapter("app:onFocusLost")
fun EditText.onFocusLost(callback: UiEventListeners.OnEditTextEventListener) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) { callback.onEvent(this) }
    }
}

@BindingAdapter("app:onFocusSelectEnd")
fun EditText.onFocusSelectEnd(selectEnd: Boolean) {
    if (selectEnd) {
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { setSelection(text.length) }
        }
    }
}
