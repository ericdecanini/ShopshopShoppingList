package com.ericthecoder.shopshopshoppinglist.library.bindingadapter

import androidx.databinding.BindingAdapter
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.ericthecoder.shopshopshoppinglist.util.listeners.UiEventListeners

@BindingAdapter("app:onFocusLost")
fun SwipeRevealLayout.onFocusLost(callback: UiEventListeners.OnSwipeRevealLayoutEvent) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) { callback.onEvent(this) }
    }
}

const val SLIDE_OFFSET_THRESHOLD = 0.015
@BindingAdapter("app:focusOnSlide")
fun SwipeRevealLayout.focusOnSlide(focusOnSlide: Boolean) {
    if (focusOnSlide)
        setSwipeListener(object: SwipeRevealLayout.SimpleSwipeListener() {
            override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                if (slideOffset > SLIDE_OFFSET_THRESHOLD)
                    view?.requestFocus()
            }
        })
}
