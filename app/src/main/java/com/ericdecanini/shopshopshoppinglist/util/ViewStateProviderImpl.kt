package com.ericdecanini.shopshopshoppinglist.util

import com.ericdecanini.entities.ViewState

class ViewStateProviderImpl : ViewStateProvider {

    override fun <T: ViewState> create(entityClass: Class<T>): T = entityClass.newInstance()
}
