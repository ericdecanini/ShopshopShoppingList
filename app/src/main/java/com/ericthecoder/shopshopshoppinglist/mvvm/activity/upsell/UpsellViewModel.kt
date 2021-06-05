package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import javax.inject.Inject

class UpsellViewModel @Inject constructor(): ViewModel() {

    private val viewEventEmitter = MutableLiveData<ViewEvent>()
    val viewEventLiveData: LiveData<ViewEvent> get() = viewEventEmitter

    fun onCtaButtonPressed() {
        // TODO: Implement
    }

    fun onBackButtonPressed() {
        viewEventEmitter.postValue(NavigateUp)
    }

    sealed class ViewEvent {
        object NavigateUp : ViewEvent()
    }
}
