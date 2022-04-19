package com.ericthecoder.shopshopshoppinglist.util.providers

import com.ericthecoder.shopshopshoppinglist.entities.ViewState

interface ViewStateProvider {

    fun <T : ViewState> create(entityClass: Class<T>): T
}
