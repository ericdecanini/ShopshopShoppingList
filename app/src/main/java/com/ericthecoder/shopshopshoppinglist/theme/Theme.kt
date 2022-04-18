package com.ericthecoder.shopshopshoppinglist.theme

import androidx.annotation.ColorRes
import com.ericthecoder.shopshopshoppinglist.R

enum class Theme(
    @ColorRes val colorRes: Int,
    @ColorRes val onColorRes: Int,
    @ColorRes val colorContainerRes: Int,
    @ColorRes val onColorContainerRes: Int,
) {
    GREEN(
        R.color.theme_color_green,
        R.color.theme_color_on_green,
        R.color.theme_color_green_container,
        R.color.theme_color_on_green_container
    ),
    RED(
        R.color.theme_color_red,
        R.color.theme_color_on_red,
        R.color.theme_color_red_container,
        R.color.theme_color_on_red_container
    ),
    BLUE(
        R.color.theme_color_blue,
        R.color.theme_color_on_blue,
        R.color.theme_color_blue_container,
        R.color.theme_color_on_blue_container
    ),
    YELLOW(
        R.color.theme_color_yellow,
        R.color.theme_color_on_yellow,
        R.color.theme_color_yellow_container,
        R.color.theme_color_on_yellow_container
    ),
    PURPLE(
        R.color.theme_color_purple,
        R.color.theme_color_on_purple,
        R.color.theme_color_purple_container,
        R.color.theme_color_on_purple_container
    ),
    PINK(
        R.color.theme_color_pink,
        R.color.theme_color_on_pink,
        R.color.theme_color_pink_container,
        R.color.theme_color_on_pink_container
    ),
}
