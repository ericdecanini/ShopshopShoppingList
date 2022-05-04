package com.ericthecoder.shopshopshoppinglist.library.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

fun Fragment.setSupportActionBar(actionBar: Toolbar) = (activity as? AppCompatActivity)?.apply {
    setSupportActionBar(actionBar)
}

fun Fragment.getSupportActionBar() = (activity as? AppCompatActivity)?.supportActionBar
