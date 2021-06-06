package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpsellViewModel @Inject constructor(
    private val billingInteractor: BillingInteractor,
    resourceProvider: ResourceProvider
): ViewModel() {

    private val viewEventEmitter = MutableLiveData<ViewEvent>()
    val viewEventLiveData: LiveData<ViewEvent> get() = viewEventEmitter

    val premiumPrice = ObservableField(resourceProvider.getString(R.string.ellipsis))

    init {
        connectToBilling()
    }

    fun onCtaButtonPressed() = viewModelScope.launch {
        val responseCode = billingInteractor.launchBillingFlow()
    }

    fun onBackButtonPressed() {
        viewEventEmitter.postValue(NavigateUp)
    }

    private fun connectToBilling() = viewModelScope.launch {
        if (billingInteractor.connectIfNeeded())
            fetchPrice()
        else
            handleConnectionFailure()
    }

    private fun fetchPrice() = viewModelScope.launch {
        billingInteractor.getPremiumSkuDetails()?.let {
            premiumPrice.set(it.price)
        }
    }

    private fun handleConnectionFailure() {

    }

    override fun onCleared() {
        billingInteractor.disconnectBillingClient()
        super.onCleared()
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
    }
}
