package com.ericthecoder.shopshopshoppinglist.library.extension

import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors

fun Fragment.setStatusBarAttrColor(@AttrRes attrRes: Int) = (activity as? AppCompatActivity)?.apply {
    window.statusBarColor = MaterialColors.getColor(getRootView(), attrRes)
}

fun Fragment.setStatusBarColor(@ColorInt color: Int) = (activity as? AppCompatActivity)?.apply {
    window.statusBarColor = color
}

fun Fragment.setSupportActionBar(actionBar: Toolbar) = (activity as? AppCompatActivity)?.apply {
    setSupportActionBar(actionBar)
}

fun Fragment.getSupportActionBar() = (activity as? AppCompatActivity)?.supportActionBar
