package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.NestedNavigationInstruction.OpenNewList
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val navigator: MainNavigator,
    private val persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter
) : ViewModel() {

    fun handleNestedInstruction(nestedNavigationInstruction: NestedNavigationInstruction) =
        when(nestedNavigationInstruction) {
            is OpenNewList -> navigator.goToList()
        }

    fun launchOnboardingIfNecessary() {
        if (!persistentStorageReader.hasOnboardingShown()) {
            navigator.goToOnboarding()
            persistentStorageWriter.setOnboardingShown(true)
        }
    }

}
