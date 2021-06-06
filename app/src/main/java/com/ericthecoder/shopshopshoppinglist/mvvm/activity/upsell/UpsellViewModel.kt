package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor.PurchaseResult
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor.PurchaseResult.*
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ErrorReason.*
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpsellViewModel @Inject constructor(
    private val billingInteractor: BillingInteractor,
    private val resourceProvider: ResourceProvider,
    private val dialogNavigator: DialogNavigator
): ViewModel() {

    private val viewEventEmitter = MutableLiveData<ViewEvent>()
    val viewEventLiveData: LiveData<ViewEvent> get() = viewEventEmitter

    val premiumPrice = ObservableField(resourceProvider.getString(R.string.ellipsis))

    init {
        connectToBilling()
    }

    fun onCtaButtonPressed() = viewModelScope.launch {
        billingInteractor.launchBillingFlow()
    }

    fun onBackButtonPressed() {
        viewEventEmitter.postValue(NavigateUp)
    }

    fun getPurchaseResultLiveData() = billingInteractor.purchaseResultLiveData

    fun handlePurchaseResult(purchaseResult: PurchaseResult) = when(purchaseResult) {
        is Success -> { /* TODO: Implement */ }
        Unavailable -> handleError(UNAVAILABLE)
        Error -> handleError(UNKNOWN)
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

    private fun handleError(reason: ErrorReason) = dialogNavigator.displayGenericDialog(
        title = resourceProvider.getString(reason.titleRes),
        message = resourceProvider.getString(reason.messageRes),
        cancellable = reason != CONNECTION_FAILURE,
        positiveText = if (reason == CONNECTION_FAILURE) resourceProvider.getString(R.string.ok) else null,
        positiveOnClick = if (reason == CONNECTION_FAILURE) ({ viewEventEmitter.value = NavigateUp }) else null
    )

    override fun onCleared() {
        billingInteractor.disconnectBillingClient()
        super.onCleared()
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
    }

    private enum class ErrorReason(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int
    ) {
        CONNECTION_FAILURE(R.string.purchase_dialog_connection_failed_title, R.string.purchase_dialog_connection_failed_message),
        UNAVAILABLE(R.string.purchase_dialog_unavailable_title, R.string.purchase_dialog_unavailable_message),
        UNKNOWN(R.string.purchase_dialog_unknown_title, R.string.purchase_dialog_unknown_message)
    }
}
