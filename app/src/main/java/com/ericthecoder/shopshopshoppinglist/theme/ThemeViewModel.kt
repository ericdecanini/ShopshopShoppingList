package com.ericthecoder.shopshopshoppinglist.theme

import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import javax.inject.Inject

class ThemeViewModel @Inject constructor(
    persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter,
) : ViewModel() {

    private val themeEmitter = MutableLiveData<Theme>()
    val theme: LiveData<Theme> = themeEmitter

    init {
        val currentTheme = persistentStorageReader.getCurrentTheme()
        themeEmitter.value = Theme.values()[currentTheme]
    }

    fun getTheme() = theme.value ?: Theme.GREEN

    fun cycleNextTheme() {
        val currentTheme = theme.value!!
        val nextTheme = if (currentTheme.ordinal == Theme.values().lastIndex) {
            Theme.values()[0]
        } else {
            Theme.values()[currentTheme.ordinal + 1]
        }

        themeEmitter.value = nextTheme
        persistentStorageWriter.setCurrentTheme(nextTheme.ordinal)
    }

    enum class Theme(@ColorRes val colorRes: Int, @ColorRes val colorVariantRes: Int) {
        GREEN(R.color.theme_color_green, R.color.theme_color_green_variant),
        RED(R.color.theme_color_red, R.color.theme_color_red_variant),
        BLUE(R.color.theme_color_blue, R.color.theme_color_blue_variant),
        YELLOW(R.color.theme_color_yellow, R.color.theme_color_yellow_variant),
        PURPLE(R.color.theme_color_purple, R.color.theme_color_purple_variant),
        PINK(R.color.theme_color_pink, R.color.theme_color_pink_variant);
    }
}