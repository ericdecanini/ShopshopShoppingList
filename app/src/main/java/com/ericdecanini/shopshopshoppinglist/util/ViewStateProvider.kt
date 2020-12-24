package com.ericdecanini.shopshopshoppinglist.util

import com.ericdecanini.entities.ViewState

interface ViewStateProvider {

    fun <T: ViewState> create(entityClass: Class<T>): T
}
