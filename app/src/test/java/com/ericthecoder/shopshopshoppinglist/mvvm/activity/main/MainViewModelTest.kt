package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val navigator: MainNavigator = mock()
    private val persistentStorageReader: PersistentStorageReader = mock()
    private val persistentStorageWriter: PersistentStorageWriter = mock()
    private val billingInteractor: BillingInteractor = mock()
    private val dialogNavigator: DialogNavigator = mock()
    private val resourceProvider: ResourceProvider = mock()

    private val viewModel = MainViewModel(
        navigator,
        persistentStorageReader,
        persistentStorageWriter,
        billingInteractor,
        dialogNavigator,
        resourceProvider,
    )

    @Test
    fun givenOnboardingHasNotShown_whenLaunchOnboardingIfNecessary_thenOnboardingLaunched() {
        given(persistentStorageReader.hasOnboardingShown()).willReturn(false)

        viewModel.launchOnboardingIfNecessary()

        verify(navigator).goToOnboarding()
        verify(persistentStorageWriter).setOnboardingShown(true)
    }

    @Test
    fun givenOnboardingHasShown_whenLaunchOnboardingIfNecessary_thenDoNothing() {
        given(persistentStorageReader.hasOnboardingShown()).willReturn(true)

        viewModel.launchOnboardingIfNecessary()

        verifyZeroInteractions(navigator)
        verifyZeroInteractions(persistentStorageWriter)
    }

    @Test
    fun givenOpenNewListInstruction_whenHandleNestedInstruction_thenOpenList() {
        val instruction = NestedNavigationInstruction.OpenNewList

        viewModel.handleNestedInstruction(instruction)

        verify(navigator).goToList()
    }
}
