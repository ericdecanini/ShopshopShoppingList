package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UpsellViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val billingInteractor: BillingInteractor = mockk {
        coEvery { connectIfNeeded() } returns true
        coEvery { getPremiumSkuDetails() } returns SkuDetails("")
    }
    private val resourceProvider: ResourceProvider = mockk {
        every { getString(any()) } returns ""
    }
    private val dialogNavigator: DialogNavigator = mockk()
    private val persistentStorageWriter: PersistentStorageWriter = mockk()
    private val persistentStorageReader: PersistentStorageReader = mockk {
        every { getPremiumStatus() } returns PremiumStatus.FREE
    }
    private val snackbarNavigator: SnackbarNavigator = mockk()

    private val viewModel = UpsellViewModel(
        billingInteractor,
        resourceProvider,
        dialogNavigator,
        persistentStorageWriter,
        persistentStorageReader,
        snackbarNavigator,
    )

    @Test
    fun whenCtaButtonPressed_thenLaunchBillingFlow() {
        coEvery { billingInteractor.launchBillingFlow() } returns Unit

        viewModel.onCtaButtonPressed()

        coVerify { billingInteractor.launchBillingFlow() }
    }

    @Test
    fun whenBackButtonPressed_thenEmitNavigateUpEvent() {

        viewModel.onBackButtonPressed()

        assertThat(viewModel.viewEventLiveData.value).isEqualTo(UpsellViewModel.ViewEvent.NavigateUp)
    }

    @Test
    fun whenGetPurchaseResultLiveData_thenReturnItFromBillingInteractor() {
        val purchaseResultLiveData: LiveData<BillingInteractor.PurchaseResult> = mockk()
        every { billingInteractor.purchaseResultLiveData } returns purchaseResultLiveData

        val result = viewModel.getPurchaseResultLiveData()

        assertThat(result).isEqualTo(purchaseResultLiveData)
    }

    @Test
    fun givenSuccessResultWithPurchasedState_whenHandlePurchaseResult_thenPremiumAcknowledgedAndSet() {
        val purchase: Purchase = mockk {
            every { purchaseState } returns Purchase.PurchaseState.PURCHASED
            every { isAcknowledged } returns false
        }
        val purchaseResult = BillingInteractor.PurchaseResult.Success(purchase)
        coEvery { billingInteractor.acknowledgePurchase(purchase) } returns BillingResult()
        every { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) } returns Unit
        every {
            dialogNavigator.displayGenericDialog(title = "",
                message = "",
                cancellable = false,
                positiveButton = any())
        } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        coVerify { billingInteractor.acknowledgePurchase(purchase) }
        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM) }
        assertThat(viewModel.premiumStatus.get()).isEqualTo(PremiumStatus.PREMIUM)
        verify {
            dialogNavigator.displayGenericDialog(title = "",
                message = "",
                cancellable = false,
                positiveButton = any())
        }
    }

    @Test
    fun givenSuccessResultWithPendingState_whenHandlePurchaseResult_thenPendingStatusSet() {
        val purchase: Purchase = mockk {
            every { purchaseState } returns Purchase.PurchaseState.PENDING
        }
        val purchaseResult = BillingInteractor.PurchaseResult.Success(purchase)
        every { persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING) } returns Unit
        every {
            dialogNavigator.displayGenericDialog(title = "",
                message = "",
                cancellable = false,
                positiveButton = any())
        } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        verify { persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING) }
        assertThat(viewModel.premiumStatus.get()).isEqualTo(PremiumStatus.PENDING)
        verify {
            dialogNavigator.displayGenericDialog(title = "",
                message = "",
                cancellable = false,
                positiveButton = any())
        }
    }

    @Test
    fun givenSuccessResultWithUnspecifiedState_whenHandlePurchaseResult_thenDisplaySnackbar() {
        val purchase: Purchase = mockk {
            every { purchaseState } returns Purchase.PurchaseState.UNSPECIFIED_STATE
        }
        val purchaseResult = BillingInteractor.PurchaseResult.Success(purchase)
        every { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        verify { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) }
    }

    @Test
    fun givenAlreadyOwnedResult_whenHandlePurchaseResult_thenDisplaySnackbar() {
        val purchaseResult = BillingInteractor.PurchaseResult.AlreadyOwned
        every { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_already_owned_message) } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        verify { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_already_owned_message) }
    }

    @Test
    fun givenUnavailableResult_whenHandlePurchaseResult_thenDisplaySnackbar() {
        val purchaseResult = BillingInteractor.PurchaseResult.Unavailable
        every { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unavailable_message) } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        verify { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unavailable_message) }
    }

    @Test
    fun givenErrorResultAndStatusFree_whenHandlePurchaseResult_thenDisplaySnackbar() {
        val purchaseResult = BillingInteractor.PurchaseResult.Error
        every { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) } returns Unit

        viewModel.handlePurchaseResult(purchaseResult)

        verify { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) }
    }

    @Test
    fun givenErrorResultAndStatusPending_whenHandlePurchaseResult_thenDoNothing() {
        val purchaseResult = BillingInteractor.PurchaseResult.Error
        every { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) } returns Unit
        viewModel.premiumStatus.set(PremiumStatus.PENDING)

        viewModel.handlePurchaseResult(purchaseResult)

        verify(inverse = true) { snackbarNavigator.displaySnackbar(R.string.purchase_dialog_unknown_message) }
    }
}
