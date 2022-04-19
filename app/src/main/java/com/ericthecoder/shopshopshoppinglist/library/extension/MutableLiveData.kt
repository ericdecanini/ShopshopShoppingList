package com.ericthecoder.shopshopshoppinglist.library.extension

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObservers() {
    this.postValue(value)
}
