package com.ericthecoder.shopshopshoppinglist.theme

import androidx.annotation.ColorRes
import com.ericthecoder.shopshopshoppinglist.R

enum class Theme(@ColorRes val colorRes: Int, @ColorRes val colorVariantRes: Int) {
    GREEN(R.color.theme_color_green, R.color.theme_color_green_variant),
    RED(R.color.theme_color_red, R.color.theme_color_red_variant),
    BLUE(R.color.theme_color_blue, R.color.theme_color_blue_variant),
    YELLOW(R.color.theme_color_yellow, R.color.theme_color_yellow_variant),
    PURPLE(R.color.theme_color_purple, R.color.theme_color_purple_variant),
    PINK(R.color.theme_color_pink, R.color.theme_color_pink_variant);
}
