package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericdecanini.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericdecanini.shopshopshoppinglist.util.navigator.Navigator
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val navigator: Navigator,
    private val persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter
) : ViewModel() {

    fun launchOnboardingIfNecessary() {
        if (!persistentStorageReader.hasOnboardingShown()) {
            navigator.goToOnboarding()
            persistentStorageWriter.setOnboardingShown(true)
        }
    }

}
