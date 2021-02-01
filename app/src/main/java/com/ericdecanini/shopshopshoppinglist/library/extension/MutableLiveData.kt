package com.ericdecanini.shopshopshoppinglist.library.extension

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObservers() {
  this.value = value
}
