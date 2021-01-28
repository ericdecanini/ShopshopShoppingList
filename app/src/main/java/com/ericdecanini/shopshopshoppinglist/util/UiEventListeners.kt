package com.ericdecanini.shopshopshoppinglist.util

import android.view.View

interface ItemClickListener<T> {
    fun onItemClicked(item: T)
}

interface FocusChangeListener<T> {
    fun onFocusChanged(view: View, item: T)
}

interface ItemCheckedListener<T> {
    fun onChecked(view: View, item: T)
}
