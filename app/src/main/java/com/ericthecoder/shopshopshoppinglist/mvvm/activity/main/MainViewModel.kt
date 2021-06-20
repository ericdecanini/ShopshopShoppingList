package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericthecoder.dependencies.android.resources.ResourceProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.library.billing.BillingInteractor
import com.ericthecoder.shopshopshoppinglist.library.billing.PremiumState
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.NestedNavigationInstruction.OpenNewList
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val navigator: MainNavigator,
    private val persistentStorageReader: PersistentStorageReader,
    private val persistentStorageWriter: PersistentStorageWriter,
    private val billingInteractor: BillingInteractor,
    private val dialogNavigator: DialogNavigator,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val premiumStatusEmitter = MutableLiveData<PremiumStatus>()
    val premiumStatusLiveData: LiveData<PremiumStatus> get() = premiumStatusEmitter

    fun handleNestedInstruction(nestedNavigationInstruction: NestedNavigationInstruction) =
        when(nestedNavigationInstruction) {
            is OpenNewList -> navigator.goToList()
        }

    fun launchOnboardingIfNecessary() {
        if (!persistentStorageReader.hasOnboardingShown()) {
            navigator.goToOnboarding()
            persistentStorageWriter.setOnboardingShown(true)
        }
    }

    fun fetchPremiumState() = viewModelScope.launch {
        if (billingInteractor.connectIfNeeded()) {
            when (billingInteractor.getPremiumState()) {
                PremiumState.FRESHLY_ACKNOWLEDGED -> {
                    showPremiumPurchasedDialog()
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

    private fun showPremiumPurchasedDialog() =  dialogNavigator.displayGenericDialog(
        title = resourceProvider.getString(R.string.purchase_dialog_purchased_title),
        message = resourceProvider.getString(R.string.purchase_dialog_purchased_message),
        positiveButton = resourceProvider.getString(R.string.ok) to {},
    )
}
