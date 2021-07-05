package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.PremiumState
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val navigator: MainNavigator = mockk(relaxUnitFun = true)
    private val persistentStorageReader: PersistentStorageReader = mockk()
    private val persistentStorageWriter: PersistentStorageWriter = mockk(relaxUnitFun = true)
    private val billingInteractor: BillingInteractor = mockk(relaxUnitFun = true) {
        coEvery { connectIfNeeded() } returns true
    }
    private val dialogNavigator: DialogNavigator = mockk(relaxUnitFun = true)
    private val resourceProvider: ResourceProvider = mockk {
        every { getString(any()) } returns ""
    }

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
        every { persistentStorageReader.hasOnboardingShown() } returns false

        viewModel.launchOnboardingIfNecessary()

        verify { navigator.goToOnboarding() }
        verify { persistentStorageWriter.setOnboardingShown(true) }
    }

    @Test
    fun givenOnboardingHasShown_whenLaunchOnboardingIfNecessary_thenDoNothing() {
        every { persistentStorageReader.hasOnboardingShown() } returns true

        viewModel.launchOnboardingIfNecessary()

        verify(inverse = true) { navigator.goToOnboarding() }
        verify(inverse = true) { persistentStorageWriter.setOnboardingShown(true) }
    }

    @Test
    fun givenOpenNewListInstruction_whenHandleNestedInstruction_thenOpenList() {
        val instruction = NestedNavigationInstruction.OpenNewList

        viewModel.handleNestedInstruction(instruction)

        verify { navigator.goToList() }
    }

    @Test
    fun givenFreshlyAcknowledged_whenFetchPremiumState_thenSetStateAndShowDialog() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.FRESHLY_ACKNOWLEDGED

        viewModel.fetchPremiumState()

        verify { dialogNavigator.displayGenericDialog(title = "", message = "", positiveButton = any()) }
        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
        assertThat(viewModel.premiumStatusLiveData.value).isEqualTo(PremiumStatus.PREMIUM)
    }

    @Test
    fun givenPremium_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.PREMIUM

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
        assertThat(viewModel.premiumStatusLiveData.value).isEqualTo(PremiumStatus.PREMIUM)
    }

    @Test
    fun givenPending_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.PENDING

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING) }
        assertThat(viewModel.premiumStatusLiveData.value).isEqualTo(PremiumStatus.PENDING)
    }

    @Test
    fun givenFree_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.FREE

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.FREE) }
        assertThat(viewModel.premiumStatusLiveData.value).isEqualTo(PremiumStatus.FREE)
    }

    @Test
    fun givenBillingFailsToConnect_whenFetchPremiumState_thenDoNothing() {
        coEvery { billingInteractor.connectIfNeeded() } returns false

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter wasNot called }
    }
}
