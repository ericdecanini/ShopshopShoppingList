package com.ericthecoder.shopshopshoppinglist.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}