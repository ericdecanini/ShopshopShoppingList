package com.ericdecanini.shopshopshoppinglist.util.providers

import com.ericdecanini.shopshopshoppinglist.entities.ViewState

interface ViewStateProvider {

    fun <T: ViewState> create(entityClass: Class<T>): T
}
