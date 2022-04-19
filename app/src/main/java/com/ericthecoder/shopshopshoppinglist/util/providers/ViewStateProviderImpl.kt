package com.ericthecoder.shopshopshoppinglist.util.providers

import com.ericthecoder.shopshopshoppinglist.entities.ViewState

class ViewStateProviderImpl : ViewStateProvider {

    override fun <T : ViewState> create(entityClass: Class<T>): T = entityClass.newInstance()
}
