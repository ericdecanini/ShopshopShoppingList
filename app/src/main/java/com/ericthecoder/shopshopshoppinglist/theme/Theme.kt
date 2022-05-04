package com.ericthecoder.shopshopshoppinglist.theme

import androidx.annotation.StyleRes
import com.ericthecoder.shopshopshoppinglist.R

enum class Theme(@StyleRes themeRes: Int?) {
    BLUE(R.style.Theme_ShopshopShoppingList_Blue),
    GREEN(R.style.Theme_ShopshopShoppingList_Green),
    ORANGE(R.style.Theme_ShopshopShoppingList_Orange),
    PINK(R.style.Theme_ShopshopShoppingList_Pink),
    PURPLE(R.style.Theme_ShopshopShoppingList_Purple),
    RED(R.style.Theme_ShopshopShoppingList_Red),
    YELLOW(R.style.Theme_ShopshopShoppingList_Yellow),
    DYNAMIC(null),
}
