package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor.PurchaseResult
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor.PurchaseResult.*
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ErrorReason.*
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpsellViewModel @Inject constructor(
    private val billingInteractor: BillingInteractor,
    private val resourceProvider: ResourceProvider,
    private val dialogNavigator: DialogNavigator,
    private val persistentStorageWriter: PersistentStorageWriter,
    private val persistentStorageReader: PersistentStorageReader,
    private val snackbarNavigator: SnackbarNavigator,
) : ViewModel() {

    private val viewEventEmitter = MutableLiveData<ViewEvent>()
    val viewEventLiveData: LiveData<ViewEvent> get() = viewEventEmitter

    val premiumPrice = ObservableField(resourceProvider.getString(R.string.ellipsis))
    val premiumStatus = ObservableField(persistentStorageReader.getPremiumStatus())

    init {
        connectToBilling()
    }

    fun onCtaButtonPressed() = viewModelScope.launch {
        billingInteractor.launchBillingFlow()
    }

    fun getTheme(): Theme {
        val themeName = persistentStorageReader.getCurrentTheme()
        return Theme.valueOf(themeName)
    }

    fun onBackButtonPressed() {
        viewEventEmitter.postValue(NavigateUp)
    }

    fun getPurchaseResultLiveData() = billingInteractor.purchaseResultLiveData

    fun handlePurchaseResult(purchaseResult: PurchaseResult) = when (purchaseResult) {
        is Success -> handlePurchase(purchaseResult.purchase)
        AlreadyOwned -> handleError(ALREADY_OWNED)
        Unavailable -> handleError(UNAVAILABLE)
        Error -> {
            handleError(UNKNOWN)
        }
    }

    private fun connectToBilling() = viewModelScope.launch {
        if (billingInteractor.connectIfNeeded())
            fetchPrice()
        else
            handleError(CONNECTION_FAILURE)
    }

    private fun fetchPrice() = viewModelScope.launch {
        billingInteractor.getPremiumSkuDetails()?.let {
            premiumPrice.set(it.price)
        }
    }

    private fun handlePurchase(purchase: Purchase) = when (purchase.purchaseState) {
        PurchaseState.PURCHASED -> handlePurchasedState(purchase)
        PurchaseState.PENDING -> handlePendingState()
        else -> handleError(UNKNOWN)
    }

    private fun handlePurchasedState(purchase: Purchase) {
        if (!purchase.isAcknowledged)
            viewModelScope.launch {
                billingInteractor.acknowledgePurchase(purchase)
            }
        persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM)
        premiumStatus.set(PremiumStatus.PREMIUM)

        dialogNavigator.displayGenericDialog(
            title = resourceProvider.getString(R.string.purchase_dialog_purchased_title),
            message = resourceProvider.getString(R.string.purchase_dialog_purchased_message),
            cancellable = false,
            positiveButton = resourceProvider.getString(R.string.ok) to { viewEventEmitter.value = NavigateUp }
        )
    }

    private fun handlePendingState() {
        persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING)
        premiumStatus.set(PremiumStatus.PENDING)

        dialogNavigator.displayGenericDialog(
            title = resourceProvider.getString(R.string.purchase_dialog_pending_title),
            message = resourceProvider.getString(R.string.purchase_dialog_pending_message),
            cancellable = false,
            positiveButton = resourceProvider.getString(R.string.ok) to { viewEventEmitter.value = NavigateUp },
        )
    }

    private fun handleError(reason: ErrorReason) =
        when {
            reason == UNKNOWN && premiumStatus.get() == PremiumStatus.PENDING -> Unit
            reason == CONNECTION_FAILURE -> showConnectionFailedDialog()
            else -> snackbarNavigator.displaySnackbar(reason.messageRes)
        }

    private fun showConnectionFailedDialog() = dialogNavigator.displayGenericDialog(
        title = resourceProvider.getString(R.string.purchase_dialog_connection_failed_title),
        message = resourceProvider.getString(R.string.purchase_dialog_connection_failed_message),
        cancellable = false,
        positiveButton = resourceProvider.getString(R.string.ok) to { viewEventEmitter.value = NavigateUp }
    )

    override fun onCleared() {
        billingInteractor.disconnectBillingClient()
        super.onCleared()
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
    }

    private enum class ErrorReason(
        @StringRes val messageRes: Int,
    ) {
        CONNECTION_FAILURE(R.string.purchase_dialog_connection_failed_message),
        ALREADY_OWNED(R.string.purchase_dialog_already_owned_message),
        UNAVAILABLE(R.string.purchase_dialog_unavailable_message),
        UNKNOWN(R.string.purchase_dialog_unknown_message)
    }
}
