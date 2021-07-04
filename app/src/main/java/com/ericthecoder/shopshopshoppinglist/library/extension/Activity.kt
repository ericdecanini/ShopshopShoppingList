package com.ericthecoder.shopshopshoppinglist.library.extension

import android.app.Activity
import android.view.View
import android.view.ViewGroup

fun Activity.getRootView(): View =
    (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
