package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.PremiumState
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import com.ericthecoder.shopshopshoppinglist.util.TestCoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class MainViewModelTest {

    private val persistentStorageReader: PersistentStorageReader = mockk()
    private val persistentStorageWriter: PersistentStorageWriter = mockk(relaxUnitFun = true)
    private val coroutineContextProvider: CoroutineContextProvider = TestCoroutineContextProvider()
    private val billingInteractor: BillingInteractor = mockk(relaxUnitFun = true)

    private val viewModel = MainViewModel(
        persistentStorageReader,
        persistentStorageWriter,
        coroutineContextProvider,
        billingInteractor,
    )

    @BeforeEach
    fun setup() {
        coEvery { billingInteractor.connectIfNeeded() } returns true
    }

    @Nested
    inner class LaunchOnboarding {

        @Test
        fun `if not previously shown, launchOnboardingIfNecessary launches onboarding flow`() {
            every { persistentStorageReader.hasOnboardingShown() } returns false

            viewModel.launchOnboardingIfNecessary()

            assertThat(viewModel.viewEvent.value).isEqualTo(GoToOnboarding)
            verify { persistentStorageWriter.setOnboardingShown(true) }
        }

        @Test
        fun `if previously shown, launchOnboardingIfNecessary does nothing`() {
            every { persistentStorageReader.hasOnboardingShown() } returns true

            viewModel.launchOnboardingIfNecessary()

            assertThat(viewModel.viewEvent.value).isNotEqualTo(GoToOnboarding)
            verify(inverse = true) { persistentStorageWriter.setOnboardingShown(true) }
        }
    }

    @Nested
    inner class NestedNavigation {

        @Test
        fun `handles nested GoToList instruction`() {
            val instruction = NestedNavigationInstruction.OpenNewList

            viewModel.handleNestedInstruction(instruction)

            assertThat(viewModel.viewEvent.value).isEqualTo(GoToList)
        }
    }

    @Nested
    inner class FetchPremiumState {

        @Test
        fun `when freshly acknowledged, fetchPremiumState saves premium state and shows dialog`() {
            coEvery { billingInteractor.getPremiumState() } returns PremiumState.FRESHLY_ACKNOWLEDGED

            viewModel.fetchPremiumState()

            assertThat(viewModel.viewEvent.value).isEqualTo(ShowPremiumPurchasedDialog)
            verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
            assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PREMIUM)
        }

        @Test
        fun `when premium, fetchPremiumState saves state`() {
            coEvery { billingInteractor.getPremiumState() } returns PremiumState.PREMIUM

            viewModel.fetchPremiumState()

            verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
            assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PREMIUM)
        }

        @Test
        fun `when pending, fetchPremiumState saves state`() {
            coEvery { billingInteractor.getPremiumState() } returns PremiumState.PENDING

            viewModel.fetchPremiumState()

            verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING) }
            assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.PENDING)
        }

        @Test
        fun `when free, fetchPremiumState saves state`() {
            coEvery { billingInteractor.getPremiumState() } returns PremiumState.FREE

            viewModel.fetchPremiumState()

            verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.FREE) }
            assertThat(viewModel.premiumStatus.value).isEqualTo(PremiumStatus.FREE)
        }

        @Test
        fun `when billing fails to connect, fetchPremiumState does nothing`() {
            coEvery { billingInteractor.connectIfNeeded() } returns false

            viewModel.fetchPremiumState()

            verify { persistentStorageWriter wasNot called }
        }
    }
}
