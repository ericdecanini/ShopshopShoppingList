package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.PremiumState
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainViewModel.ViewEvent.*
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

    private val persistentStorageReader: PersistentStorageReader = mockk()
    private val persistentStorageWriter: PersistentStorageWriter = mockk(relaxUnitFun = true)
    private val billingInteractor: BillingInteractor = mockk(relaxUnitFun = true) {
        coEvery { connectIfNeeded() } returns true
    }

    private val viewModel = MainViewModel(
        persistentStorageReader,
        persistentStorageWriter,
        billingInteractor,
    )

    @Test
    fun givenOnboardingHasNotShown_whenLaunchOnboardingIfNecessary_thenOnboardingLaunched() {
        every { persistentStorageReader.hasOnboardingShown() } returns false

        viewModel.launchOnboardingIfNecessary()

        assertThat(viewModel.viewEvent.value).isEqualTo(GoToOnboarding)
        verify { persistentStorageWriter.setOnboardingShown(true) }
    }

    @Test
    fun givenOnboardingHasShown_whenLaunchOnboardingIfNecessary_thenDoNothing() {
        every { persistentStorageReader.hasOnboardingShown() } returns true

        viewModel.launchOnboardingIfNecessary()

        assertThat(viewModel.viewEvent.value).isNotEqualTo(GoToOnboarding)
        verify(inverse = true) { persistentStorageWriter.setOnboardingShown(true) }
    }

    @Test
    fun givenOpenNewListInstruction_whenHandleNestedInstruction_thenOpenList() {
        val instruction = NestedNavigationInstruction.OpenNewList

        viewModel.handleNestedInstruction(instruction)

        assertThat(viewModel.viewEvent.value).isEqualTo(GoToList)
    }

    @Test
    fun givenFreshlyAcknowledged_whenFetchPremiumState_thenSetStateAndShowDialog() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.FRESHLY_ACKNOWLEDGED

        viewModel.fetchPremiumState()

        assertThat(viewModel.viewEvent.value).isEqualTo(ShowPremiumPurchasedDialog)
        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
        assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PREMIUM)
    }

    @Test
    fun givenPremium_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.PREMIUM

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
        assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PREMIUM)
    }

    @Test
    fun givenPending_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.PENDING

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING) }
        assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PENDING)
    }

    @Test
    fun givenFree_whenFetchPremiumState_thenSetState() {
        coEvery { billingInteractor.getPremiumState() } returns PremiumState.FREE

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.FREE) }
        assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.FREE)
    }

    @Test
    fun givenBillingFailsToConnect_whenFetchPremiumState_thenDoNothing() {
        coEvery { billingInteractor.connectIfNeeded() } returns false

        viewModel.fetchPremiumState()

        verify { persistentStorageWriter wasNot called }
    }
}
