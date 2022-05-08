package com.ericthecoder.shopshopshoppinglist.library.extension

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.google.android.material.color.MaterialColors

fun Activity.getRootView(): View =
    (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

fun Activity.setTheme(theme: Theme) {
    when (theme) {
        Theme.BLUE -> setTheme(R.style.Theme_ShopshopShoppingList_Blue)
        Theme.GREEN -> setTheme(R.style.Theme_ShopshopShoppingList_Green)
        Theme.ORANGE -> setTheme(R.style.Theme_ShopshopShoppingList_Orange)
        Theme.PINK -> setTheme(R.style.Theme_ShopshopShoppingList_Pink)
        Theme.PURPLE -> setTheme(R.style.Theme_ShopshopShoppingList_Purple)
        Theme.RED -> setTheme(R.style.Theme_ShopshopShoppingList_Red)
        Theme.YELLOW -> setTheme(R.style.Theme_ShopshopShoppingList_Yellow)
        Theme.DYNAMIC -> Unit
    }
}

fun Activity.initColoredBackgroundAndStatusBar(root: View) {
    val backgroundColor = MaterialColors.getColor(getRootView(), R.attr.backgroundColor)
    val primaryColor = MaterialColors.getColor(getRootView(), R.attr.colorPrimary)
    val layeredColor = ColorUtils.blendARGB(backgroundColor, primaryColor, 0.05F)
    root.setBackgroundColor(layeredColor)
    window.statusBarColor = layeredColor
}