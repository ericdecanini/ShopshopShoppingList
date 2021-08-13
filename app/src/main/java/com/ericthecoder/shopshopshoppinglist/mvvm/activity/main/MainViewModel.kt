package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.PremiumState
import com.ericthecoder.shopshopshoppinglist.library.livedata.MutableSingleLiveEvent
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.NestedNavigationInstruction.OpenNewList
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter,
    private val billingInteractor: BillingInteractor,
) : ViewModel() {

    private val premiumStatusEmitter = MutableLiveData<PremiumStatus>()
    val premiumStatus: LiveData<PremiumStatus> get() = premiumStatusEmitter

    private val viewEventEmitter = MutableSingleLiveEvent<ViewEvent>()
    val viewEvent: LiveData<ViewEvent> get() = viewEventEmitter

    fun handleNestedInstruction(nestedNavigationInstruction: NestedNavigationInstruction) =
        when (nestedNavigationInstruction) {
            is OpenNewList -> viewEventEmitter.value = ViewEvent.GoToList
        }

    fun launchOnboardingIfNecessary() {
        if (!persistentStorageReader.hasOnboardingShown()) {
            viewEventEmitter.value = ViewEvent.GoToOnboarding
            persistentStorageWriter.setOnboardingShown(true)
        }
    }

    fun fetchPremiumState() = viewModelScope.launch {
        if (billingInteractor.connectIfNeeded()) {
            when (billingInteractor.getPremiumState()) {
                PremiumState.FRESHLY_ACKNOWLEDGED -> {
                    viewEventEmitter.value = ViewEvent.ShowPremiumPurchasedDialog
                    persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM)
                    premiumStatusEmitter.value = PremiumStatus.PREMIUM
                }
                PremiumState.PREMIUM -> {
                    persistentStorageWriter.setPremiumStatus(PremiumStatus.PREMIUM)
                    premiumStatusEmitter.value = PremiumStatus.PREMIUM
                }
                PremiumState.PENDING -> {
                    persistentStorageWriter.setPremiumStatus(PremiumStatus.PENDING)
                    premiumStatusEmitter.value = PremiumStatus.PENDING
                }
                PremiumState.FREE -> {
                    persistentStorageWriter.setPremiumStatus(PremiumStatus.FREE)
                    premiumStatusEmitter.value = PremiumStatus.FREE
                }
            }
        }
    }

    sealed class ViewEvent {
        object GoToList : ViewEvent()
        object GoToOnboarding : ViewEvent()
        object ShowPremiumPurchasedDialog : ViewEvent()
    }
}
